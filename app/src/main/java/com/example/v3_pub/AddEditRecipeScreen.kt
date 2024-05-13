package com.example.v3_pub

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

//THis screen is where i can add and edit my recipes. it is using JSON local file storage


class AddEditRecipeScreen : Fragment() {
    private var recipeId: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_edit_recipe, container, false)
        val nameEditText = view.findViewById<EditText>(R.id.recipeNameEditText)
        val ingredientsEditText = view.findViewById<EditText>(R.id.ingredientsEditText)
        val instructionsEditText = view.findViewById<EditText>(R.id.instructionsEditText)
        val saveButton = view.findViewById<Button>(R.id.saveButton)

        recipeId = arguments?.getInt("recipeId", -1)
        if (recipeId != null && recipeId != -1) {
            loadRecipe(nameEditText, ingredientsEditText, instructionsEditText)
        }

        saveButton.setOnClickListener {
            val name = nameEditText.text.toString().trim()
            val ingredients = ingredientsEditText.text.toString().trim()
            val instructions = instructionsEditText.text.toString().trim()

            if (name.isNotEmpty() && ingredients.isNotEmpty() && instructions.isNotEmpty()) {
                if (saveRecipe(name, ingredients, instructions)) {
                    findNavController().popBackStack()
                } else {
                    Toast.makeText(context, "Error saving recipe", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    //callng the DataUtil. How to load is specificed there but the actual loading is done here

    private fun loadRecipe(nameEditText: EditText, ingredientsEditText: EditText, instructionsEditText: EditText) {
        val recipesJson = RecipeDataUtil.loadRecipes(requireContext())
        if (recipesJson.isBlank()) {
            Toast.makeText(context, "No recipes found", Toast.LENGTH_SHORT).show()
            return
        }


        //GSOn is called for json serialisation/deserialisation
        //loop through the list of Recipe which has its own Data class. grabs them in GSON/JSON format

        val gson = Gson()
        val type = object : TypeToken<List<Recipe>>() {}.type    //define what is being deserialisied which is the mutable (modifyable) list of Recipe
        val recipes: List<Recipe> = gson.fromJson(recipesJson, type)
        recipes.find { it.id == recipeId }?.let { recipe ->  //loop through and find each Recipe
            nameEditText.setText(recipe.name)
            ingredientsEditText.setText(recipe.ingredients)
            instructionsEditText.setText(recipe.instructions)
        }
    }

    //

    private fun saveRecipe(name: String, ingredients: String, instructions: String): Boolean {
        try {
            val gson = Gson()
            val type = object : TypeToken<MutableList<Recipe>>() {}.type //define the type again
            val recipesJson = RecipeDataUtil.loadRecipes(requireContext())
            val recipes: MutableList<Recipe> = if (recipesJson.isBlank()) mutableListOf() else gson.fromJson(recipesJson, type) //actually do the deserialisation
            val newRecipe = Recipe(recipeId ?: System.currentTimeMillis().toInt(), name, ingredients, instructions) //put what has been deserialisaed into new Recipe

            val index = recipes.indexOfFirst { it.id == recipeId }  //do this for every recipe
            if (index != -1) {
                recipes[index] = newRecipe
            } else {
                recipes.add(newRecipe)
            }

            RecipeDataUtil.saveRecipes(requireContext(), gson.toJson(recipes))
            return true
        } catch (ex: Exception) {
            Toast.makeText(context, "Failed to save recipe: ${ex.message}", Toast.LENGTH_LONG).show()
            return false
        }
    }
}
