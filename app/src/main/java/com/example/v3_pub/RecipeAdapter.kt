package com.example.v3_pub


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

//RecipeAdapter has  RecyclerView.Adapter to display and interact with a list of the Recipe objects.



class RecipeAdapter(var recipes: MutableList<Recipe>,
                    private val onEditClick: (Recipe) ->
                    Unit, private val onDeleteClick: (Recipe) -> Unit)
    : RecyclerView.Adapter<RecipeAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recipe_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        //set text

        val recipe = recipes[position]
        holder.nameTextView.text = recipe.name
        holder.ingredientsTextView.text = recipe.ingredients
        holder.instructionsTextView.text = recipe.instructions

        //handle the button clicks

        holder.editButton.setOnClickListener { onEditClick(recipe) }
        holder.deleteButton.setOnClickListener {
            recipes.removeAt(position)
            notifyItemRemoved(position)
            onDeleteClick(recipe)
        }
    }

    override fun getItemCount() = recipes.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.nameTextView)
        val ingredientsTextView: TextView = view.findViewById(R.id.ingredientsTextView)
        val instructionsTextView: TextView = view.findViewById(R.id.instructionsTextView)
        val editButton: Button = view.findViewById(R.id.editButton)
        val deleteButton: Button = view.findViewById(R.id.deleteButton)
    }
}


