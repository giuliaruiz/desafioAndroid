package com.example.desafio

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Calendar
import java.util.Locale

class AgendamentoActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var etDate: EditText
    private lateinit var etTime: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agendamento)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        etDate = findViewById(R.id.etDate)
        etTime = findViewById(R.id.etTime)
        val btnSchedule = findViewById<Button>(R.id.btnSchedule)

        // Seleção de data
        etDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val datePicker = DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    val selectedDate = "$dayOfMonth/${month + 1}/$year"
                    etDate.setText(selectedDate)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePicker.show()
        }

        // Seleção de horário
        etTime.setOnClickListener {
            val calendar = Calendar.getInstance()
            val timePicker = TimePickerDialog(
                this,
                { _, hourOfDay, minute ->
                    val selectedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute)
                    etTime.setText(selectedTime)
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
            )
            timePicker.show()
        }

        // Lógica de agendamento
        btnSchedule.setOnClickListener {
            val date = etDate.text.toString()
            val time = etTime.text.toString()
            val userId = auth.currentUser?.uid

            if (date.isNotEmpty() && time.isNotEmpty() && userId != null) {
                val appointmentData = hashMapOf(
                    "date" to date,
                    "time" to time,
                    "patientId" to userId
                )

                db.collection("appointments")
                    .add(appointmentData)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Consulta agendada com sucesso!", Toast.LENGTH_SHORT).show()
                        finish() // Fecha a activity após o agendamento
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Erro ao agendar a consulta.", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}