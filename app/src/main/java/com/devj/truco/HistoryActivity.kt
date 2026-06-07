package com.devj.truco


import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.devj.truco.databinding.HistoryactivityBinding
import com.devj.truco.databinding.MainactivityBinding
import com.devj.truco.ui.theme.TrucoTheme




class HistoryActivity : ComponentActivity() {
    private lateinit var binding: HistoryactivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = HistoryactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val hostVitories:Int = intent.getIntExtra("etHostVictories",0)
        binding.etPointsHost.setText(hostVitories.toString())

        val visitorVitories:Int = intent.getIntExtra("etVisitorVictories",0)
        binding.etPointsVisitor.setText(visitorVitories.toString())

        // Recebendo os nomes e setando nas labels com verificação de vazio
        val tvLabelHostName = intent.getStringExtra("tvLabelHostName")
        binding.tvLabelHostName.text = if (tvLabelHostName.isNullOrBlank()) "Jogador 1" else tvLabelHostName

        val tvLabelVisitorName = intent.getStringExtra("tvLabelVisitorName")
        binding.tvLabelVisitorName.text = if (tvLabelVisitorName.isNullOrBlank()) "Jogador 2" else tvLabelVisitorName

        binding.buttonBack.setOnClickListener {
            finish() // Apenas fecha a tela atual para voltar à anterior
        }
    }
}
