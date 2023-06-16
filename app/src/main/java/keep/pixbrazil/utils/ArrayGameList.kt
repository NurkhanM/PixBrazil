package keep.pixbrazil.utils

object ArrayGameList {

    private val QUESTIONS_1 = arrayOf(
        "Бразилия"  ,
        "Аргентина" ,
        "Франция"   ,
        "Испания"
    )

    private val QUESTIONS_2 = arrayOf(
        "Реал Мадрид",
        "Барселона",
        "Бавария Мюнхен",
        "Манчестер Юнайтед"
    )
    private val QUESTIONS_3 = arrayOf(
        "Лионель Месси",
        "Криштиану Роналду",
        "Неймар",
        "Роберт Левандовски"
    )
    private val QUESTIONS_4 = arrayOf(
        "Германия",
        "Бразилия",
        "Аргентина",
        "Италия"
    )
    private val QUESTIONS_5 = arrayOf(
        "Лионель Месси",
        "Роберт Левандовски",
        "Карим Бензема",
        "Лука Модрич"
    )

    val ARRAY_LIST_FOOTBALL = arrayListOf(
        ArrayList(QUESTIONS_1.toList()),
        ArrayList(QUESTIONS_2.toList()),
        ArrayList(QUESTIONS_3.toList()),
        ArrayList(QUESTIONS_4.toList()),
        ArrayList(QUESTIONS_5.toList()),
    )
}