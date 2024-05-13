package com.example.v3_pub

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException

class RecipeListFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RecipeAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_recipe_list, container, false)

        //  RecyclerView
        recyclerView = view.findViewById(R.id.recipesRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Load recipes and initialize adapter with edit and delete actions form nav_graph
        val recipes = loadRecipes()
        adapter = RecipeAdapter(recipes,
            onEditClick = { recipe ->
                // Pass recipe ID to the edit fragment
                val bundle = Bundle()
                bundle.putInt("recipeId", recipe.id)
                findNavController().navigate(R.id.action_recipeListFragment_to_addEditRecipeFragment, bundle)
            },
            onDeleteClick = { recipe ->
                deleteRecipe(recipe)
            }
        )
        recyclerView.adapter = adapter

        // Floating action button setup for adding new recipes
        val addRecipeFab: FloatingActionButton = view.findViewById(R.id.addRecipeFab)
        addRecipeFab.setOnClickListener {
            findNavController().navigate(R.id.action_recipeListFragment_to_addEditRecipeFragment)
        }

        return view
    }

    private fun loadRecipes(): MutableList<Recipe> {
        val jsonString = readJsonDataFromFile()
        if (jsonString.isNullOrEmpty()) return mutableListOf()
        val gson = Gson()
        val type = object : TypeToken<List<Recipe>>() {}.type
        return gson.fromJson(jsonString, type)
    }

    private fun readJsonDataFromFile(): String? {
        return try {
            requireContext().openFileInput("recipes.json").bufferedReader().use { it.readText() }
        } catch (e: IOException) {
            null
        }
    }

    private fun deleteRecipe(recipe: Recipe) {
        // Remove recipe and update
        val recipes = loadRecipes().filter { it.id != recipe.id }.toMutableList()
        saveRecipes(recipes)
        adapter.recipes = recipes
        adapter.notifyDataSetChanged()
    }

    private fun saveRecipes(recipes: List<Recipe>) {
        val recipesJson = Gson().toJson(recipes)
        requireContext().openFileOutput("recipes.json", Context.MODE_PRIVATE).use {
            it.write(recipesJson.toByteArray())
        }
    }
}
