package com.example.boos

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }
    // Java implementation of above approach

    // Java implementation of above approach
    internal object GFG {
        // Finds reverse of given num x.
        fun reverseNum(x: Int): Int {
            val s = Integer.toString(x)
            var str = ""
            for (i in s.length - 1 downTo 0) {
                str = str + s[i]
            }
            return str.toInt()
        }
        fun isMysteryNumber(n: Int): Boolean {
            for (i in 1..n / 2) {
                // if found print the pair, return
                val j = reverseNum(i)
                if (i + j == n) {
                    println("$i $j")
                    return true
                }
            }
            println("Not a Mystery Number")
            return false
        }

        @JvmStatic
        fun main(args: Array<String>) {
            val n = 121
            isMysteryNumber(n)
        }
    }

// This code is contributed by ihritik


}