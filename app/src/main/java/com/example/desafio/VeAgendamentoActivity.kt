package com.example.desafio

import AgendamentoAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class VeAgendamentoActivity : AppCompatActivity() {
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var recyclerViewAgendamentos: RecyclerView
    private lateinit var agendamentoAdapter: AgendamentoAdapter
    private val agendamentoList = mutableListOf<agendamentoData>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.exibir_agendamento)

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        recyclerViewAgendamentos = findViewById(R.id.recyclerViewAppointments)

        // Configuração do RecyclerView
        agendamentoAdapter = AgendamentoAdapter(agendamentoList)
        recyclerViewAgendamentos.layoutManager = LinearLayoutManager(this)
        recyclerViewAgendamentos.adapter = agendamentoAdapter

        loadAgendamentos()
    }

    private fun loadAgendamentos() {
        val userId = auth.currentUser?.uid

        if (userId != null) {
            db.collection("appointments")
                .whereEqualTo("patientId", userId) // Filtra os agendamentos do usuário logado
                .get()
                .addOnSuccessListener { documents ->
                    agendamentoList.clear()
                    for (document in documents) {
                        val date = document.getString("date") ?: ""
                        val time = document.getString("time") ?: ""
                        agendamentoList.add(agendamentoData(date, time))
                    }
                    agendamentoAdapter.notifyDataSetChanged()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Erro ao carregar agendamentos.", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "Usuário não autenticado.", Toast.LENGTH_SHORT).show()
        }
    }
}