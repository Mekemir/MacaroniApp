package com.game.macaroniapp

import android.annotation.SuppressLint
import android.content.res.Resources

@SuppressLint("UseCompatLoadingForDrawables")
fun getRecipesData(resources: Resources, unlocked: ArrayList<Int>): ArrayList<CookingItemData> {
    val listOfIcons = arrayListOf<CookingItemData>()
    //pastas
    listOfIcons.add(CookingItemData(3,resources.getDrawable(R.drawable.pesto_pene), "Pesto Penne Pasta", arrayListOf(resources.getDrawable(R.drawable.penne),resources.getDrawable( R.drawable.alfredo), resources.getDrawable(R.drawable.tortellini)), "three", !unlocked.contains(3)))
    listOfIcons.add(CookingItemData(4,resources.getDrawable(R.drawable.mac_cheese), "Mac And Cheese", arrayListOf(resources.getDrawable(R.drawable.orzo),resources.getDrawable( R.drawable.cavatappi), resources.getDrawable(R.drawable.pipe_rigate)), "one", !(unlocked.contains(4))))
    listOfIcons.add(CookingItemData(5,resources.getDrawable(R.drawable.spagheti_carbonara), "Spaghetti Carbonara", arrayListOf(resources.getDrawable(R.drawable.spaghetti),resources.getDrawable( R.drawable.mafaldine), resources.getDrawable(R.drawable.cannelloni)), "three", !unlocked.contains(5)))
    listOfIcons.add(CookingItemData(6,resources.getDrawable(R.drawable.shrimp_alfredo), "Shrimp Alfredo", arrayListOf(resources.getDrawable(R.drawable.alfredo),resources.getDrawable( R.drawable.cavatappi), resources.getDrawable(R.drawable.bucatini)), "two", !unlocked.contains(6)))
    listOfIcons.add(CookingItemData(7,resources.getDrawable(R.drawable.fetucine_alfredo), "Fettucine Alfredo", arrayListOf(resources.getDrawable(R.drawable.fettuccine),resources.getDrawable( R.drawable.penne), resources.getDrawable(R.drawable.orzo)), "three", !unlocked.contains(7)))
    listOfIcons.add(CookingItemData(8,resources.getDrawable(R.drawable.spagheti_bolognese), "Spaghetti Bolognese", arrayListOf(resources.getDrawable(R.drawable.spaghetti),resources.getDrawable( R.drawable.bucatini), resources.getDrawable(R.drawable.cannelloni)), "three", !unlocked.contains(8)))
    listOfIcons.add(CookingItemData(9,resources.getDrawable(R.drawable.chicken_pene_pasta), "Chicken penne pasta", arrayListOf(resources.getDrawable(R.drawable.farfalle),resources.getDrawable( R.drawable.penne), resources.getDrawable(R.drawable.stelline)), "Three", !unlocked.contains(9)))
    listOfIcons.add(CookingItemData(10,resources.getDrawable(R.drawable.farfale_pata), "Farfalle Pata with mushroom rose sauce", arrayListOf(resources.getDrawable(R.drawable.spaghetti),resources.getDrawable( R.drawable.orzo), resources.getDrawable(R.drawable.farfalle)), "Two", !unlocked.contains(10)))
    listOfIcons.add(CookingItemData(11,resources.getDrawable(R.drawable.lemon_butter_pasta), "Lemon butter Pasta", arrayListOf(resources.getDrawable(R.drawable.farfalle),resources.getDrawable( R.drawable.stelline), resources.getDrawable(R.drawable.spaghetti)), "two", !unlocked.contains(11)))
    listOfIcons.add(CookingItemData(12,resources.getDrawable(R.drawable.stuffed_shells), "Stuffed Shells", arrayListOf(resources.getDrawable(R.drawable.spaghetti),resources.getDrawable( R.drawable.cannelloni), resources.getDrawable(R.drawable.conchigliette)), "one", !unlocked.contains(12)))
    listOfIcons.add(CookingItemData(13,resources.getDrawable(R.drawable.mussels_pasta), "Mussels Pasta in tomato sauce", arrayListOf(resources.getDrawable(R.drawable.stelline),resources.getDrawable( R.drawable.spaghetti), resources.getDrawable(R.drawable.alfredo)), "one", !unlocked.contains(13)))
// endof pastas

    // soup
    listOfIcons.add(CookingItemData(14,resources.getDrawable(R.drawable.lemon_chichen_soupe), "Lemon Chicken Orzo Soup", arrayListOf(resources.getDrawable(R.drawable.orzo),resources.getDrawable( R.drawable.alfredo), resources.getDrawable(R.drawable.conchiglioni)), "three", !unlocked.contains(14)))
    listOfIcons.add(CookingItemData(15,resources.getDrawable(R.drawable.creamy_soup), "Creamy Tortellini Soup with sausage", arrayListOf(resources.getDrawable(R.drawable.tortellini),resources.getDrawable( R.drawable.stelline), resources.getDrawable(R.drawable.alfredo)), "two", !unlocked.contains(15)))
// endof soup

    // salads
    listOfIcons.add(CookingItemData(16,resources.getDrawable(R.drawable.italian_salad), "Italian pasta Salad", arrayListOf(resources.getDrawable(R.drawable.orzo),resources.getDrawable( R.drawable.pipe_rigate), resources.getDrawable(R.drawable.tortellini)), "three", !unlocked.contains(16)))
    listOfIcons.add(CookingItemData(17,resources.getDrawable(R.drawable.mediterian_salad), "Mediterranean Orzo pasta Salad", arrayListOf(resources.getDrawable(R.drawable.orzo),resources.getDrawable( R.drawable.pipe_rigate), resources.getDrawable(R.drawable.spaghetti)), "three", !unlocked.contains(17)))
    listOfIcons.add(CookingItemData(18,resources.getDrawable(R.drawable.greek_salad), "Greek Pasta Salad", arrayListOf(resources.getDrawable(R.drawable.alfredo),resources.getDrawable( R.drawable.tortellini), resources.getDrawable(R.drawable.pipe_rigate)), "two", !unlocked.contains(18)))
// endof salads

    return listOfIcons
}

fun getUnlockedLevels(string: String): ArrayList<Int> {
    val lockedArray: ArrayList<Int> = arrayListOf()
    val x: List<String> = string.split("?")
    if (x.isEmpty()) return lockedArray
    for (v in x) {
        if (!v.isNullOrEmpty()) {
            lockedArray.add(v.toInt())
        }
    }
    return lockedArray
}

