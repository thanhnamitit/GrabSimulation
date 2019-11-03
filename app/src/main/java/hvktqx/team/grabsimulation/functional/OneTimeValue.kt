package hvktqx.team.grabsimulation.functional

class OneTimeValue<T>(private val value: T) {
  var used = false
  operator fun invoke(): T? {
    return if (!used) {
      used = true
      value
    } else {
      null
    }
  }
}
