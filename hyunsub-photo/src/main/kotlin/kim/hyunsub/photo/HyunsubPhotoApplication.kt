package kim.hyunsub.photo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["kim.hyunsub.photo", "kim.hyunsub.common"])
class HyunsubPhotoApplication

fun main(args: Array<String>) {
	runApplication<HyunsubPhotoApplication>(*args)
}

// TODO 여러 사용자가 사용할 수 있게 만들기
// TODO 앨범 없이도 사진을 업로드 할 수 있게 하기. 폴더 구조를 많이 바꿔야 할 듯
// TODO 사용자 별로 보유할 수 있는 사진 수나 총 용량을 제한하기
// TODO 어드민 권한이 없어도 앨범 생성 권한 만들기
// TODO Album, Photo ID를 String 으로 바꾸기
