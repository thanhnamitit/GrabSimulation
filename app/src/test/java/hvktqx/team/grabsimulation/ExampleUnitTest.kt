package hvktqx.team.grabsimulation

import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    data class Response(val token: String, val items: List<String>)

    @Test
    fun addition_isCorrect() {
        val responseObservable = Observable.just(Response("a", listOf("1", "2", "3")))
        val tokenObservable = responseObservable.map { it.token }
        val itemsObservable =
            responseObservable.map { item -> item.items + " he" }.flatMap { items ->
                Observable.fromArray(items).map { itemHe ->
                    itemHe.map { "$it after transform" }
                }
            }

        Observable.zip<String, List<String>, Response>(
            tokenObservable,
            itemsObservable,
            BiFunction<String, List<String>, Response> { token, items ->
                Response(token, items)
            }).subscribe {
            print(it)
        }
    }
}
