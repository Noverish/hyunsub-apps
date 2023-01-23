package kim.hyunsub.comic.controller

import kim.hyunsub.comic.config.ComicConstants
import kim.hyunsub.comic.model.ComicOrganizeConsolidateParams
import kim.hyunsub.comic.model.ComicOrganizeFileParams
import kim.hyunsub.comic.model.ComicOrganizeConsolidateResult
import kim.hyunsub.comic.model.ComicOrganizeFileResult
import kim.hyunsub.common.api.ApiCaller
import kim.hyunsub.common.api.model.ApiRenameBulkParamData
import kim.hyunsub.common.api.model.ApiRenameBulkParams
import kim.hyunsub.common.web.annotation.Authorized
import kim.hyunsub.common.web.error.ErrorCode
import kim.hyunsub.common.web.error.ErrorCodeException
import mu.KotlinLogging
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import kotlin.io.path.Path
import kotlin.io.path.extension
import kotlin.io.path.nameWithoutExtension

/**
 * 만화 폴더 이름 정리
 */
@Authorized(["admin"])
@RestController
@RequestMapping("/api/v1/organize")
class ComicOrganizeController(
	private val apiCaller: ApiCaller,
) {
	private val log = KotlinLogging.logger { }

	/**
	 * 에피소드 폴더 내 파일들을 순서에 따라 이름을 형식에 맞게 수정
	 */
	@PostMapping("/file")
	fun file(@RequestBody params: ComicOrganizeFileParams): ComicOrganizeFileResult {
		log.debug { "[Comic Organize File] params=$params" }

		val comicPath = Path(ComicConstants.BASE_PATH, params.title).toString()
		val episodes = apiCaller.readdir(comicPath).sorted()

		val renames = mutableListOf<ApiRenameBulkParamData>()

		for (episode in episodes) {
			val episodePath = Path(comicPath, episode).toString()
			val files = apiCaller.readdir(episodePath).sorted()

			for ((i, file) in files.withIndex()) {
				val expected = i.toString().padStart(4, '0')
				val actual = Path(file).nameWithoutExtension
				val ext = Path(file).extension

				if (expected != actual) {
					val from = Path(episode, "$actual.$ext").toString()
					val to = Path(episode, "$expected.$ext").toString()
					renames.add(ApiRenameBulkParamData(from, to))
					log.debug { "[Comic Organize File] $from -> $to" }
				}
			}
		}

		if (!params.dryRun) {
			apiCaller.renameBulk(
				ApiRenameBulkParams(
					path = comicPath,
					renames = renames,
				)
			)
		}

		return ComicOrganizeFileResult(
			renames = renames.map { it.from + " -> " + it.to }
		)
	}

	/**
	 * 100-1화, 100-2화 이렇게 나뉘어 있는 것을 한 폴더로 합침
	 */
	@PostMapping("/consolidate")
	fun consolidate(@RequestBody params: ComicOrganizeConsolidateParams): ComicOrganizeConsolidateResult {
		log.debug { "[Comic Organize Consolidate] params=$params" }
		val regex = Regex(params.pattern)

		val comicPath = Path(ComicConstants.BASE_PATH, params.title).toString()
		val candidates = apiCaller.readdir(comicPath).sorted()
			.filter { it.contains(regex) }
		val candidateMap = candidates.groupBy { regex.find(it)!!.groupValues[1] }

		val renames = mutableListOf<ApiRenameBulkParamData>()

		for (siblings in candidateMap.values) {
			if (siblings.size == 1) {
				throw ErrorCodeException(ErrorCode.INTERNAL_SERVER_ERROR, siblings.toString())
			}

			var i = 0
			val firstSibling = siblings.first()

			for (sibling in siblings) {
				val siblingPath = Path(comicPath, sibling).toString()
				apiCaller.readdir(siblingPath).forEach {
					val filePath = Path(sibling, it).toString()

					val newFileName = (i++).toString().padStart(4, '0') + "." + Path(it).extension
					val newFilePath = Path(firstSibling, newFileName).toString()

					if (filePath != newFilePath) {
						log.debug { "[Comic Organize Consolidate] file: $filePath -> $newFilePath" }
						renames.add(ApiRenameBulkParamData(filePath, newFilePath))
					}
				}
			}
		}

		val renameParams = ApiRenameBulkParams(
			path = comicPath,
			renames = renames.toList(),
		)

		val renameFolderParams = ApiRenameBulkParams(
			path = comicPath,
			renames = candidateMap.values.map { siblings ->
				val from = siblings.first()
				val to = from.replace(regex, "$1${params.suffix}")
				log.debug { "[Comic Organize Consolidate] folder: $from -> $to" }
				ApiRenameBulkParamData(from, to)
			}
		)

		val removes = candidateMap.values.flatMap { it.drop(1) }
			.map { Path(comicPath, it).toString() }
		removes.forEach {
			log.debug { "[Comic Organize Consolidate] remove: $it" }
		}

		if (!params.dryRun) {
			apiCaller.renameBulk(renameParams)
			apiCaller.renameBulk(renameFolderParams)
			apiCaller.removeBulk(removes)
		}

		return ComicOrganizeConsolidateResult(
			rename = renameParams.renames.map {
				Path(renameParams.path, it.from).toString() + " -> " + Path(renameParams.path, it.to).toString()
			},
			renameFolder = renameFolderParams.renames.map {
				Path(renameFolderParams.path, it.from).toString() + " -> " + Path(renameFolderParams.path, it.to).toString()
			},
			removes = removes,
		)
	}
}
