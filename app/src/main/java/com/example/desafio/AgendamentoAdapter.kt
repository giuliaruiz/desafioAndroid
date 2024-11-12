import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.desafio.R
import com.example.desafio.agendamentoData

class AgendamentoAdapter(private val agendamentoList: List<agendamentoData>) :
    RecyclerView.Adapter<AgendamentoAdapter.AgendamentoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AgendamentoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_agendamento, parent, false)
        return AgendamentoViewHolder(view)
    }

    override fun onBindViewHolder(holder: AgendamentoViewHolder, position: Int) {
        val agendamento = agendamentoList[position]
        holder.tvDate.text = agendamento.date
        holder.tvTime.text = agendamento.time
    }

    override fun getItemCount(): Int = agendamentoList.size

    class AgendamentoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvDate: TextView = itemView.findViewById(R.id.tvDate)
        val tvTime: TextView = itemView.findViewById(R.id.tvTime)
    }
}
