package com.devj.truco


import android.app.Activity
import android.os.Bundle
import android.content.Intent
import androidx.activity.ComponentActivity
import com.devj.truco.databinding.PlayersactivityBinding


class PlayersActivity : ComponentActivity() {
    private lateinit var binding: PlayersactivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = PlayersactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Recebe os nomes atuais da MainActivity para preencher os campos
        val currentHostName = intent.getStringExtra("tvLabelHostName")
        val currentVisitorName = intent.getStringExtra("tvLabelVisitorName")

        binding.etNamePlayerHost.setText(currentHostName)
        binding.etNamePlayerVisitor.setText(currentVisitorName)


        binding.buttonSave.setOnClickListener {

            val tvLabelHostName = binding.etNamePlayerHost.text.toString()
            val tvLabelVisitorName = binding.etNamePlayerVisitor.text.toString()

            val intent = Intent()
            intent.putExtra("tvLabelHostName", tvLabelHostName)
            intent.putExtra("tvLabelVisitorName", tvLabelVisitorName)

            setResult(Activity.RESULT_OK, intent)
            finish()
        }

        binding.buttonBack.setOnClickListener {
            finish()
        }

    }
}
