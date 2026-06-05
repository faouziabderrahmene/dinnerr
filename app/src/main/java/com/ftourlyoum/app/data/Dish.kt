package com.ftourlyoum.app.data

// ===================================================================
// Dish.kt — Data Model & Dish List
// ===================================================================
// Contains the Dish data class and the modifiable list of Tunisian
// dishes. To add or remove dishes, simply edit the 'dishes' list.
// Each dish has an Arabic name and an emoji icon.
// ===================================================================

/**
 * Represents a single Tunisian dish with a name and emoji icon.
 *
 * @property name  The Arabic name of the dish
 * @property emoji An emoji icon representing the dish
 */
data class Dish(
    val name: String,
    val emoji: String
)

/**
 * ═══════════════════════════════════════════════════════════════
 * MODIFIABLE DISH LIST
 * ═══════════════════════════════════════════════════════════════
 * To add a new dish:    Add a Dish("name", "emoji") entry below
 * To remove a dish:     Delete the corresponding line
 * To reorder:           Move entries up or down in the list
 * ═══════════════════════════════════════════════════════════════
 */
val dishes = listOf(
    Dish("كسكسي", "🥘"),
    Dish("مرقة جلبانة", "🍲"),
    Dish("مرقة كالامار", "🦑"),
    Dish("مرقة بطاطة", "🥔"),
    Dish("مرقة خضرة", "🥬"),
    Dish("ملوخية", "🌿"),
    Dish("كفتاجي", "🍳"),
    Dish("لازانيا", "🧀"),
    Dish("مقرونة بالحم", "🍝"),
    Dish("مقرونة بالدجاج", "🐔"),
    Dish("روز جربي", "🍚"),
    Dish("روز ازعر", "🌾"),
    Dish("طاجين", "🫕"),
    Dish("شربة", "🥣"),
    Dish("بريك", "🥟"),
    Dish("عجة", "🥚"),
    Dish("شكشوكة", "🍅"),
    Dish("قلاية", "🫑")
)

/**
 * Returns a random dish from the list, avoiding the previous selection
 * to prevent picking the same dish twice in a row.
 *
 * @param previousDish The dish that was picked last (null if first pick)
 * @return A randomly selected [Dish]
 */
fun getRandomDish(previousDish: Dish? = null): Dish {
    // If only one dish, return it
    if (dishes.size <= 1) return dishes.first()

    // Keep picking until we get a different dish
    var pick: Dish
    do {
        pick = dishes.random()
    } while (pick.name == previousDish?.name)

    return pick
}