package br.com.alura.ceep.ui.recyclerview.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.alura.ceep.BR
import br.com.alura.ceep.R
import br.com.alura.ceep.databinding.ItemNotaBinding
import br.com.alura.ceep.model.Nota
import br.com.alura.ceep.ui.databinding.NotaData
import br.com.alura.ceep.ui.extensions.carregaImagem
import kotlinx.android.synthetic.main.item_nota.view.*

class ListaNotasAdapter(
    private val context: Context,
    var onItemClickListener: (notaSelecionada: Nota) -> Unit = {}
) : ListAdapter<Nota, ListaNotasAdapter.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)

        //ItemNotaBinding é o nome da classe que o compilador gera caso queira mudar o nome, adicionar a propriedade class no layout na tag data
        val viewDataBinding = ItemNotaBinding.inflate(inflater, parent, false)
        return ViewHolder(viewDataBinding).also {
            viewDataBinding.lifecycleOwner = it
        }
    }

    override fun onViewAttachedToWindow(holder: ViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder.marcaComoAtivo()
    }

    override fun onViewDetachedFromWindow(holder: ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.marcaComoDesativo()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { nota ->
            holder.vincula(nota)
        }
    }

    inner class ViewHolder(private val viewDataBinding: ItemNotaBinding) : RecyclerView.ViewHolder(viewDataBinding.root), LifecycleOwner {

        private lateinit var nota: Nota

        private val registry = LifecycleRegistry(this)

        init {
            viewDataBinding.listener = this
            registry.markState(Lifecycle.State.INITIALIZED)
        }

        fun marcaComoAtivo(){
            registry.markState(Lifecycle.State.STARTED)
        }

        fun marcaComoDesativo(){
            registry.markState(Lifecycle.State.DESTROYED)
        }


        override fun getLifecycle(): Lifecycle = registry

        fun clicaNaNota(view: View){
            if (::nota.isInitialized) {
                onItemClickListener(nota)
            }
        }

        fun vincula(nota: Nota) {
            this.nota = nota
            viewDataBinding.nota = NotaData(nota)
        }

    }
}

object DiffCallback : DiffUtil.ItemCallback<Nota>() {
    override fun areItemsTheSame(
        oldItem: Nota,
        newItem: Nota
    ) = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Nota, newItem: Nota) = oldItem == newItem

}

