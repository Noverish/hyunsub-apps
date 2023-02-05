package kim.hyunsub.test

import com.navercorp.fixturemonkey.LabMonkey
import com.navercorp.fixturemonkey.api.introspector.JavaTypeArbitraryGenerator
import com.navercorp.fixturemonkey.kotlin.KotlinPlugin
import com.navercorp.fixturemonkey.kotlin.giveMeBuilder
import net.jqwik.api.Arbitraries
import net.jqwik.api.Arbitrary
import net.jqwik.api.arbitraries.StringArbitrary

val monkey: LabMonkey = LabMonkey.labMonkeyBuilder()
    .plugin(KotlinPlugin())
    .javaTypeArbitraryGenerator(CustomArbitraryGenerator())
    .build()

/**
 * 무조건 비어있지 않은 문자열을 생성함
 */
val stringMonkey: Arbitrary<String> = monkey.giveMeBuilder<String>()
    .also { it.setPostCondition { it2 -> it2.isNotEmpty() } }
    .build()

private class CustomArbitraryGenerator : JavaTypeArbitraryGenerator {
    override fun strings(): StringArbitrary =
        Arbitraries.strings()
            .withCharRange('a', 'z')
            .withCharRange('A', 'Z')
            .withCharRange('0', '9')
            .withChars('_')
}
