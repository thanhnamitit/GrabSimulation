package hvktqx.team.grabsimulation.functional

class Wrapper<T>(val value: T?) {
  companion object {
    fun <T> empty(): Wrapper<T> = Wrapper(null)
  }
}
