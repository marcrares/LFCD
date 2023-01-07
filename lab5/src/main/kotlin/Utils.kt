import java.lang.NumberFormatException

object Utils {

    fun String.tryParse() : Int = try {
        this.toInt()
    } catch (ex: NumberFormatException) {
        -1
    }
}