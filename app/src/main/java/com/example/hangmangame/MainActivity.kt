package com.example.hangmangame

import android.app.AlertDialog
import android.os.Bundle
import android.widget.Button
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.tooling.preview.Preview
import com.example.hangmangame.ui.theme.HangmanGameTheme

class MainActivity : ComponentActivity() {

        private lateinit var wordToGuess: String
        private lateinit var wordDisplay: TextView
        private lateinit var hangmanView: HangmanView
        private var guessedLetters = mutableSetOf<Char>()
        private var remainingTurns = 6

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main)

            wordDisplay = findViewById(R.id.wordTextView)
            hangmanView = findViewById(R.id.hangmanView)
            val newGameButton: Button = findViewById(R.id.newGameButton)
            val letterButtons: GridLayout = findViewById(R.id.letterButtons)

            setupNewGame()
            setupLetterButtons(letterButtons)

            newGameButton.setOnClickListener {
                setupNewGame()
            }
        }

        private fun setupNewGame() {
            wordToGuess = getRandomWord().uppercase()
            guessedLetters.clear()
            remainingTurns = 6
            updateWordDisplay()
            hangmanView.updateWrongGuesses(0)
        }

        private fun setupLetterButtons(gridLayout: GridLayout) {
            val alphabet = 'A'..'Z'
            gridLayout.removeAllViews()

            alphabet.forEach { letter ->
                val button = Button(this).apply {
                    text = letter.toString()
                    setOnClickListener { onLetterClicked(letter) }
                }
                gridLayout.addView(button)
            }
        }

        private fun onLetterClicked(letter: Char) {
            guessedLetters.add(letter)
            updateWordDisplay()

            if (!wordToGuess.contains(letter)) {
                remainingTurns--
                hangmanView.updateWrongGuesses(6 - remainingTurns)
            }

            checkGameOver()
        }

        private fun updateWordDisplay() {
            wordDisplay.text = wordToGuess.map { if (guessedLetters.contains(it)) it else '_' }.joinToString(" ")
        }

        private fun checkGameOver() {
            if (remainingTurns == 0) {
                showGameOverDialog("Loser!")
            } else if (!wordDisplay.text.contains('_')) {
                showGameOverDialog("Congratulation!")
            }
        }

        private fun showGameOverDialog(message: String) {
            AlertDialog.Builder(this)
                .setTitle("Game Over!")
                .setMessage(message)
                .setPositiveButton("Ok") { _, _ -> setupNewGame() }
                .show()
        }

        private fun getRandomWord(): String {
            val wordList = listOf("Cat", "Dog", "Sky","HELLO")
            return wordList.random()
        }
}

