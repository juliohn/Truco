package com.devj.truco


import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import com.devj.truco.databinding.MainactivityBinding
import com.devj.truco.ui.theme.TrucoTheme




class MainActivity : ComponentActivity() {
    private lateinit var binding: MainactivityBinding
    private var etVisitorVictories = 0
    private var etHostVictories = 0
    private var tvLabelHostName = ""
    private var tvLabelVisitorName = ""

    private val getPlayersResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            tvLabelHostName = data?.getStringExtra("tvLabelHostName") ?: ""
            tvLabelVisitorName = data?.getStringExtra("tvLabelVisitorName") ?: ""

            binding.tvLabelHostName.text = tvLabelHostName
            binding.tvLabelVisitorName.text = tvLabelVisitorName

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = MainactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        savedInstanceState?.let {
            etHostVictories = it.getInt("etHostVictories")
            etVisitorVictories = it.getInt("etVisitorVictories")
            tvLabelHostName = it.getString("tvLabelHostName", "")
            tvLabelVisitorName = it.getString("tvLabelVisitorName", "")

            if (tvLabelHostName.isNotEmpty()) binding.tvLabelHostName.text = tvLabelHostName
            if (tvLabelVisitorName.isNotEmpty()) binding.tvLabelVisitorName.text = tvLabelVisitorName
        }


    // - list of all actions buttons sum points to host
        val listButtonsHost = listOf(
            binding.buttonOneHost,
            binding.buttonThreeHost,
            binding.buttonSixHost,
            binding.buttonNineHost,
            binding.buttonTwelveHost
        )

        // set value to each button clicked host,
        listButtonsHost.forEach { button ->
            val pointsForButton = when (button.id) {
                R.id.buttonOneHost -> 1
                R.id.buttonThreeHost -> 3
                R.id.buttonSixHost -> 6
                R.id.buttonNineHost -> 9
                R.id.buttonTwelveHost -> 12
                else -> 0
            }

            // - when simple click increment, when double click decrease host
            var clickCount = 0
            button.setOnClickListener {
                clickCount++
                if (clickCount == 1) {
                    button.postDelayed({
                        if (clickCount == 1) {
                            managerPoints(pointsForButton, isHost = true, isIncrement = true)
                        } else {
                            managerPoints(pointsForButton, isHost = true, isIncrement = false)
                        }
                        clickCount = 0
                    }, 200)
                }
            }
        }

    // - list of all actions buttons sum points to visitor
        val listButtonsVisitor = listOf(
            binding.buttonOneVisitor,
            binding.buttonThreeVisitor,
            binding.buttonSixVisitor,
            binding.buttonNineVisitor,
            binding.buttonTwelveVisitor
        )

        // set value to each button clicked vistiro,
        listButtonsVisitor.forEach { button ->
            val pointsForButton = when (button.id) {
                R.id.buttonOneVisitor -> 1
                R.id.buttonThreeVisitor -> 3
                R.id.buttonSixVisitor -> 6
                R.id.buttonNineVisitor -> 9
                R.id.buttonTwelveVisitor -> 12
                else -> 0
            }

            // - when simple click increment, when double click decrease visitor
            var clickCount = 0
            button.setOnClickListener {
                clickCount++
                if (clickCount == 1) {
                    button.postDelayed({
                        if (clickCount == 1) {
                            managerPoints(pointsForButton, isHost = false, isIncrement = true)
                        } else {
                            managerPoints(pointsForButton, isHost = false, isIncrement = false)
                        }
                        clickCount = 0
                    }, 200)
                }
            }
        }


    // - Button History - Pass values params of victories and names to other activity
        binding.buttonHistory.setOnClickListener {
            val intent = Intent(this@MainActivity, HistoryActivity::class.java )
            intent.putExtra("etVisitorVictories", etVisitorVictories)
            intent.putExtra("etHostVictories", etHostVictories)
            intent.putExtra("tvLabelHostName", tvLabelHostName)
            intent.putExtra("tvLabelVisitorName", tvLabelVisitorName)
            startActivity(intent)
        }

    // - button Reset History
        binding.buttonResetHistory.setOnClickListener {
            Toast.makeText(this, getString(R.string.reset_success), Toast.LENGTH_LONG).show()

            resetVictories()
        }
    // - button Players Name
        binding.buttonPlayersNames.setOnClickListener {
            val intent = Intent(this@MainActivity, PlayersActivity::class.java)
            intent.putExtra("tvLabelHostName", tvLabelHostName)
            intent.putExtra("tvLabelVisitorName", tvLabelVisitorName)
            getPlayersResult.launch(intent)
        }

    }

    private fun managerPoints(points: Int, isHost: Boolean, isIncrement: Boolean = true) {
        val etPoints = if (isHost) binding.etPointsHost else binding.etPointsVisitor
        val currentPoints = etPoints.text.toString().toIntOrNull() ?: 0

        val newPoints = if (isIncrement) {
            currentPoints + points
        } else {
            currentPoints - points
        }

        val finalPoints = newPoints.coerceIn(0, 12)
        etPoints.setText(finalPoints.toString())

        if (finalPoints >= 12) {
            val winnerName: String
            val loserName: String

            if (isHost) {
                winnerName = binding.tvLabelHostName.text.toString()
                loserName = binding.tvLabelVisitorName.text.toString()
                etHostVictories++
            } else {
                winnerName = binding.tvLabelVisitorName.text.toString()
                loserName = binding.tvLabelHostName.text.toString()
                etVisitorVictories++
            }
            showWinnerAlert(winnerName, loserName)
        }
    }

    private fun showWinnerAlert(winner: String, loser: String) {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.loser_message, loser))
            .setMessage(getString(R.string.winner_message, winner))
            .setCancelable(false)
            .setPositiveButton(getString(R.string.next_game)) { dialog, which ->
                resetPoints()
            }
            .create()
            .show()
    }

    private fun resetPoints() {
        binding.etPointsHost.setText(getString(R.string.double_zero))
        binding.etPointsVisitor.setText(getString(R.string.double_zero))
    }


    private fun resetVictories(){
        etHostVictories = 0
        etVisitorVictories = 0
        tvLabelHostName = ""
        tvLabelVisitorName = ""
        binding.tvLabelHostName.text = getString(R.string.default_player_1)
        binding.tvLabelVisitorName.text = getString(R.string.default_player_2)
        resetPoints()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("etHostVictories", etHostVictories)
        outState.putInt("etVisitorVictories", etVisitorVictories)
        outState.putString("tvLabelHostName", tvLabelHostName)
        outState.putString("tvLabelVisitorName", tvLabelVisitorName)
    }

}
