package com.example.v3_pub

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException
import java.lang.reflect.Type

object RecipeDataUtil {
    private const val FILE_NAME = "recipes.json"

    // Load recipes as a String
    fun loadRecipes(context: Context): String {
        return try {
            context.openFileInput(FILE_NAME).bufferedReader().use { it.readText() }
        } catch (e: IOException) {
            "[]"  // Return an empty JSON array as a string if there's an error
        }
    }

    // Save JSON data from a List<Recipe>
    fun saveRecipes(context: Context, recipesJson: String) {
        context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE).use {
            it.write(recipesJson.toByteArray())
        }
    }

    // Delete a specific recipe
    fun deleteRecipe(context: Context, recipeId: Int) {
        val recipesJson = loadRecipes(context)
        val gson = Gson()
        val type: Type = object : TypeToken<List<Recipe>>() {}.type
        val recipes: MutableList<Recipe> = gson.fromJson(recipesJson, type) ?: mutableListOf()

        val iterator = recipes.iterator()
        while (iterator.hasNext()) {
            val recipe = iterator.next()
            if (recipe.id == recipeId) {
                iterator.remove()
                break
            }
        }

        saveRecipes(context, gson.toJson(recipes))
    }
}
