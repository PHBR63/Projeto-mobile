/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.navigation

import androidx.databinding.DataBindingUtil
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.android.navigation.databinding.FragmentGameBinding

class GameFragment : Fragment() {
    data class Question(
            val text: String,
            val answers: List<String>)

    // The first answer is the correct one.  We randomize the answers before showing the text.
    // All questions must have four answers.  We'd want these to contain references to string
    // resources so we could internationalize. (or better yet, not define the questions in code...)
    private val questions: MutableList<Question> = mutableListOf(
            Question(text = "Qual é o réptil que muda de cor conforme o lugar em que está?",
                    answers = listOf("Camaleão", "Sapo", "Lagarto", "Jacaré")),

            Question(text = "Qual foi o recurso utilizado inicialmente pelo homem para explicar a origem das coisas?",
                    answers = listOf("A Mitologia ", "A Astronomia", "A Matemática", "A Biologia")),

            Question(text = "Um adulto sadio tem quantos dentes na boca?",
                    answers = listOf("32", "18", "24", "36")),

            Question(text = "Quais destas doenças são sexualmente transmissíveis?",
                    answers = listOf("Gonorreia, clamídia e sífilis", "Aids, tricomoníase e ebola",
                            "Chikungunya, aids e herpes genital", "Boludismo, cistite e gonorreia")),

            Question(text = "Qual o nome dado ao estado da água em forma de gelo?",
                    answers = listOf("Sólido", "Líquido", "Vaporoso", "Gasoso")),

            Question(text = "Qual destes elementos se forma dentro de uma ostra?",
                    answers = listOf("Pérola", "Diamante", "Rubi", "Carvão")),

            Question(text = "Qual o maior animal terrestre?",
                    answers = listOf("Elefante africano", "Baleia Azul", "Dinossauro", "Girafa")),

            Question(text = "As pessoas de qual tipo sanguíneo são consideradas doadores universais?",
                    answers = listOf("Tipo O", "Tipo A", "Tipo AB", "Tipo B")),

            Question(text = "Como se chamam os vasos que transportam sangue do coração para a periferia do corpo?",
                    answers = listOf("Artérias", "Veias", "Ventrículos", "Átrios")),

            Question(text = "De onde é a invenção do chuveiro elétrico?",
                    answers = listOf(" Brasil ", "Inglaterra", "Itália", "França"))
    )

    lateinit var currentQuestion: Question
    lateinit var answers: MutableList<String>
    private var questionIndex = 0
    private val numQuestions = 5

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val binding = DataBindingUtil.inflate<FragmentGameBinding>(
                inflater, R.layout.fragment_game, container, false)

        randomizeQuestions()

        binding.game = this

        binding.submitButton.setOnClickListener { view: View ->
            val checkedId = binding.questionRadioGroup.checkedRadioButtonId
            if (-1 != checkedId) {
                var answerIndex = 0
                when (checkedId) {
                    R.id.secondAnswerRadioButton -> answerIndex = 1
                    R.id.thirdAnswerRadioButton -> answerIndex = 2
                    R.id.fourthAnswerRadioButton -> answerIndex = 3
                }
                if (answers[answerIndex] == currentQuestion.answers[0]) {
                    questionIndex++
                    if (questionIndex < numQuestions) {
                        currentQuestion = questions[questionIndex]
                        setQuestion()
                        binding.invalidateAll()
                    } else {
                        view.findNavController().navigate(GameFragmentDirections.actionGameFragmentToGameWonFragment(numQuestions,questionIndex))
                    }
                } else {
                    view.findNavController().navigate(GameFragmentDirections.actionGameFragmentToGameOverFragment())
                }
            }
        }
        return binding.root
    }

    private fun randomizeQuestions() {
        questions.shuffle()
        questionIndex = 0
        setQuestion()
    }

    private fun setQuestion() {
        currentQuestion = questions[questionIndex]
        answers = currentQuestion.answers.toMutableList()
        answers.shuffle()
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.title_android_trivia_question, questionIndex + 1, numQuestions)
    }
}
