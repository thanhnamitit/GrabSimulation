package hvktqx.team.grabsimulation

import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    @Test
    fun addition_isCorrect() {
        main()
    }

    fun main() {
        validateAddresses(
            arrayOf(
                ['000.012.234.23', '666.666.23.23', '.213.123.23.32', '23.45.22.32.',\
            666.666.23.23                       '272:2624:235e:3bc2:c46d:682:5d46:638g', '1:22:333:4444']
        .213.123.23.32
        23.45.22.32.
        272:2624:235e:3bc2:c46d:682:5d46:638g
        1:22:333:4444
            )
        ).forEach {
            println(it)
        }
    }


    fun validateAddresses(addresses: Array<String>): Array<String> {
        return addresses.map { check(it) }.toTypedArray()

    }

    fun check(input: String): String {
        return when {
            isV4(input) -> "IPv4"
            isV6(input) -> "IPv6"
            else -> "Neither"
        }
    }

    fun isV4(input: String): Boolean {
        var result = true
        if (!input.contains(".")) result = false
        val nums = input.split(".")
        if (nums.size != 4) result = false
        if (result && nums.any {
                it.length > 3 || it.isEmpty()
            }) result = false
        if (result && nums.any {
                it.any { !(it in ('0'..'9')) }
            }) result = false
        if (result && nums.any {
                val num = it.toInt()
                num.toString().length != it.length && num >= 8
            }) result = false
        return result


    }

    fun isV6(inputTmp: String): Boolean {
        var input = inputTmp
        var result = true
        var needCheckLength = true
        if (input.length > 39 || !input.contains(":")) result = false
        if (input.contains("::")) needCheckLength = false
        while (input.contains("::")) input = input.replace("::", ":")
        val nums = input.split(":")
        if (needCheckLength && nums.size != 8) result = false
        if (result && nums.any {
                it.length > 4
            }) result = false
        if (result && nums.any {
                it.any { !(it in ('0'..'9')) && !(it in ('a'..'f')) }
            }) result = false
        return result
    }

}
