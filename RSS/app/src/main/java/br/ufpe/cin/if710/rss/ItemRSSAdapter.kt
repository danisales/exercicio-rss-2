package br.ufpe.cin.if710.rss

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.itemlista.view.*
import android.content.Intent
import android.net.Uri

class ItemRSSAdapter(private val items: List<ItemRSS>, private val c : Context)
    : RecyclerView.Adapter<ItemRSSAdapter.ViewHolder>(){

    override fun getItemCount() : Int = items.size

    // Infla o layout definido em itemlista.xml
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(c).inflate(R.layout.itemlista, parent, false)
        return ViewHolder(view)
    }

    // Coloca as informações de cada item no TextView
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val i = items.get(position)
        holder?.title?.text = i.title
        holder?.pubDate?.text = i.pubDate
        // Abre link no browser ao clicar em um item
        holder?.itemView.setOnClickListener(){ openLink(i.link) }
    }

    private fun openLink(url: String){
        c.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    }

    class ViewHolder(item: View): RecyclerView.ViewHolder(item), View.OnClickListener{
        val title = item.item_titulo
        val pubDate = item.item_data

        override fun onClick(v: View?) {}
    }
}