package kim.hyunsub.common.util

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

@OptIn(ExperimentalContracts::class)
fun CharSequence?.isNotEmpty(): Boolean {
	contract {
		returns(true) implies (this@isNotEmpty != null)
	}
	return !this.isNullOrEmpty()
}
