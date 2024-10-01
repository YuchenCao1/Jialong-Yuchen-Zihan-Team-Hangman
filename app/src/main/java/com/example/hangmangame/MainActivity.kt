package com.example.hangmangame

import android.app.AlertDialog
import android.os.Bundle
import android.widget.*
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {

    private lateinit var wordToGuess: String
    private lateinit var wordDisplay: TextView
    private lateinit var hangmanView: ImageView
    private lateinit var letterButtons: GridLayout
    private var guessedLetters = mutableSetOf<Char>()
    private var remainingTurns = 6
    private var wrongGuesses = 6 - remainingTurns
    private var hintClickCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        wordDisplay = findViewById(R.id.wordTextView)
        hangmanView = findViewById(R.id.hangmanView)
        val newGameButton: Button = findViewById(R.id.newGameButton)
        letterButtons = findViewById(R.id.letterButtons)

        val hintButton: Button? = findViewById(R.id.hintButton)

        setupNewGame()
        setupLetterButtons(letterButtons)

        newGameButton.setOnClickListener {
            setupNewGame()
        }

        hintButton?.setOnClickListener {
            showHint()
        }
    }

    private fun setupNewGame() {
        wordToGuess = getRandomWord().uppercase()
        guessedLetters.clear()
        remainingTurns = 6
        hintClickCount = 0
        updateWordDisplay()
        wrongGuesses = 0

        for (i in 0 until letterButtons.childCount) {
            val button = letterButtons.getChildAt(i) as? Button
            button?.isEnabled = true
        }
    }

    private fun setupLetterButtons(gridLayout: GridLayout) {
        val alphabet = 'A'..'Z'
        gridLayout.removeAllViews()

        alphabet.forEach { letter ->
            val button = Button(this).apply {
                text = letter.toString()
                layoutParams = GridLayout.LayoutParams().apply {
                    width = 100
                    height = 100
                    setMargins(4, 4, 4, 4)
                }
                setOnClickListener { onLetterClicked(letter) }
            }
            gridLayout.addView(button)
        }
    }

    private fun onLetterClicked(letter: Char) {
        guessedLetters.add(letter)
        updateWordDisplay()
        disableLetterButton(letter)

        if (!wordToGuess.contains(letter)) {
            remainingTurns--
            updateHangmanImage()
        }

        checkGameOver()
    }

    private fun updateHangmanImage() {
        val resId = resources.getIdentifier("hangman$wrongGuesses", "drawable", packageName)
        hangmanView.setImageResource(resId)
    }

    private fun updateWordDisplay() {
        wordDisplay.text = wordToGuess.map { if (guessedLetters.contains(it)) it else '_' }.joinToString(" ")
    }

    private fun checkGameOver() {
        if (remainingTurns == 0) {
            showGameOverDialog("Lose! The word is \"$wordToGuess\".")
        } else if (!wordDisplay.text.contains('_')) {
            showGameOverDialog("Win!")
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
        val wordList = listOf(
            "CAT", "DOG", "SKY", "HELLO", "APPLE", "HOUSE",
            "LION", "BIRD", "TREE", "FISH", "BOOK", "STAR",
            "MOON", "SUN", "RAIN", "WIND", "FIRE", "ICE",
            "EARTH", "WATER"
        )
        return wordList.random()
    }

    private fun showHint() {
        when (hintClickCount) {
            0 -> {
                val unrevealedLetters = wordToGuess.filter { it !in guessedLetters }
                if (unrevealedLetters.isNotEmpty()) {
                    val hintLetter = unrevealedLetters.random()
                    guessedLetters.add(hintLetter)
                    updateWordDisplay()
                    disableLetterButton(hintLetter)
                    Toast.makeText(this, "Hint: Revealed the letter \"$hintLetter\"", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "No hints available", Toast.LENGTH_SHORT).show()
                }
                hintClickCount++
            }
            1 -> {
                if (remainingTurns <= 1) {
                    Toast.makeText(this, "Hint not available", Toast.LENGTH_SHORT).show()
                    return
                }

                val incorrectLetters = ('A'..'Z').filter { it !in wordToGuess && it !in guessedLetters }
                if (incorrectLetters.isNotEmpty()) {
                    val lettersToDisable = incorrectLetters.shuffled().take((incorrectLetters.size + 1) / 2)
                    lettersToDisable.forEach { disableLetterButton(it) }
                    remainingTurns--
                    wrongGuesses = 6 - remainingTurns
                    Toast.makeText(this, "Hint: Disabled some incorrect letters", Toast.LENGTH_SHORT).show()
                    hintClickCount++
                    checkGameOver()
                } else {
                    Toast.makeText(this, "No letters to disable", Toast.LENGTH_SHORT).show()
                }
            }
            2 -> {
                if (remainingTurns <= 1) {
                    Toast.makeText(this, "Hint not available", Toast.LENGTH_SHORT).show()
                    return
                }

                val vowels = listOf('A', 'E', 'I', 'O', 'U')
                val vowelsInWord = vowels.filter { it in wordToGuess && it !in guessedLetters }

                if (vowelsInWord.isNotEmpty()) {
                    vowelsInWord.forEach {
                        guessedLetters.add(it)
                        disableLetterButton(it)
                    }
                    updateWordDisplay()
                    vowels.forEach { disableLetterButton(it) }
                    remainingTurns--
                    wrongGuesses = 6 - remainingTurns
                    Toast.makeText(this, "Hint: Revealed all vowels", Toast.LENGTH_SHORT).show()
                    hintClickCount++
                    checkGameOver()
                } else {
                    Toast.makeText(this, "No unrevealed vowels", Toast.LENGTH_SHORT).show()
                }
            }
            else -> {
                Toast.makeText(this, "No more hints available", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun disableLetterButton(letter: Char) {
        for (i in 0 until letterButtons.childCount) {
            val button = letterButtons.getChildAt(i) as? Button
            if (button?.text.toString().firstOrNull() == letter) {
                button?.isEnabled = false
                break
            }
        }
    }
}
