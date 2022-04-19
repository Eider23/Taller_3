package com.example.madlibs

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class InputActivity : AppCompatActivity() {
    private lateinit var textTitle: TextView
    private lateinit var textInput: EditText

    private lateinit var buttonOk: Button

    private var numberOfWords = 0

    private var wordsInFile = mutableListOf<String>()

    private val words = mutableListOf<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input)

        initViews()

        val file = resources.openRawResource(R.raw.madlib1_tarzan).bufferedReader().use {
            it.readText()
        }
        wordsInFile = file.split(" ").toMutableList()

        wordsInFile.map { word ->
            if (word[0].toString() == "<") {
                words.add(word)
            }
        }
        askWords(words.first())
    }

    private fun askWords(word: String) {
        textTitle.text = "Ingrese la palabra para $word"
        textInput.hint = word
        textInput.text.clear()
    }

    private fun initViews() {
        textTitle = findViewById(R.id.inpuTitle)
        textInput = findViewById(R.id.inputText)
        buttonOk = findViewById(R.id.inputButton)
        buttonOk.setOnClickListener {
            if (textInput.text.toString() != "") {
                if (numberOfWords < words.size) {
                    words[numberOfWords] = textInput.text.toString()
                    numberOfWords++
                }
            }
            if (numberOfWords < words.size) {
                askWords(words[numberOfWords])
                Log.d("WORDSSSSS", "$words")
            }
            if (numberOfWords == words.size) {
                val intent = Intent(this, ResultActivity::class.java).apply {
                    putExtra("result", finishText())
                }
                startActivity(intent)
            }
        }
    }

    private fun finishText(): String {
        var text = ""
        var i = 0
        wordsInFile.forEachIndexed { index, wordFilled ->
            if (wordFilled[0] == '<') {
                wordsInFile[index] = words[i]
                i++
            }
        }
        wordsInFile.map {
            text += "$it "
        }
        return text
    }
}