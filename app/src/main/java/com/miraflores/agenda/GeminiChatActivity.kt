package com.miraflores.agenda
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.launch

class GeminiChatActivity : AppCompatActivity() {
    // !!! IMPORTANTE: PON TU API KEY DE GEMINI AQUI !!!
    val API_KEY = "AIzaSyD5b-F3rWLQ6HorgJyPrG7QT5hnj5oDPXA"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gemini)

        val tv = findViewById<TextView>(R.id.tvRespuesta)
        val et = findViewById<EditText>(R.id.etPregunta)
        val btn = findViewById<Button>(R.id.btnEnviar)
        val bar = findViewById<ProgressBar>(R.id.progress)

        // Usamos el modelo Flash que es rápido
        val model = GenerativeModel("gemini-1.5-flash", API_KEY)

        btn.setOnClickListener {
            val q = et.text.toString()
            if (q.isNotEmpty()) {
                bar.visibility = View.VISIBLE
                btn.isEnabled = false
                tv.append("\n\nTú: $q")
                et.setText("")

                lifecycleScope.launch {
                    try {
                        val response = model.generateContent(q)
                        tv.append("\nIA: ${response.text}")
                    } catch (e: Exception) {
                        tv.append("\nError: ${e.message}")
                    } finally {
                        bar.visibility = View.GONE
                        btn.isEnabled = true
                    }
                }
            }
        }
    }
}