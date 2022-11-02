import java.io.File

class LanguageSpec {

    private val keyWords = listOf(
        "integer", "array", "string", "boolean", "until", "is",
        "of", "verify", "otherwise", "read", "write", "val"
    )
    private val operators = listOf("+", "-", "*", "/", "%", "<", ">", "<=", ">=", "=", "<-", "&", "|")
    private val separators = listOf(";", "(", ")", " ", "[", "]", "\n", "\t")

    fun getType(token: String) : Type? {
        if (isSeparator(token))
            return Type.SEPARATOR
        if (isOperator(token))
            return Type.OPERATOR
        if (isKeyWord(token))
            return Type.KEYWORD
        if (isConstant(token))
            return Type.CONSTANT
        if (isIdentifier(token))
            return Type.IDENTIFIER
        return null
    }

    private fun isIdentifier(token: String): Boolean =
        token.matches(Regex("^[a-zA-Z]([a-z|A-Z|0-9])*\$"))

    private fun isConstant(token: String): Boolean =
        token.matches(Regex("^[0-9]+$")) ||
                token.matches(Regex("^\"[a-zA-Z0-9?!@#$%^&*()=+.>< ]*\"$"))

    private fun isSeparator(token: String): Boolean = separators.contains(token)

    private fun isOperator(token: String): Boolean = operators.contains(token)

    private fun isKeyWord(token: String): Boolean = keyWords.contains(token)

}

enum class Type {
    IDENTIFIER,
    CONSTANT,
    SEPARATOR,
    OPERATOR,
    KEYWORD
}