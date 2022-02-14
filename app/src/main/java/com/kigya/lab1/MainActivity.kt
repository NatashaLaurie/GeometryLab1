package com.kigya.lab1

import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.button.MaterialButton
import com.kigya.lab1.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private var listener: OnBottomSheetCallbacks? = null
    private var mBottomSheetBehavior: BottomSheetBehavior<View?>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                viewModel.isLoading.value
            }.also {
                setOnExitAnimationListener { viewProvider ->
                    viewProvider.iconView
                        .animate()
                        .setDuration(500L)
                        .alpha(0f)
                        .withEndAction() {
                            viewProvider.remove()
                        }
                        .start()
                }
            }

            _binding = ActivityMainBinding.inflate(layoutInflater)
            val view = binding.root
            setContentView(view)
            supportActionBar?.elevation = 0f
            setToggleMenuButtons()
        }
    }

    override fun onStart() {
        super.onStart()

        configureBackdrop()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    fun setOnBottomSheetCallbacks(onBottomSheetCallbacks: OnBottomSheetCallbacks) {
        this.listener = onBottomSheetCallbacks
    }

    fun closeBottomSheet() {
        mBottomSheetBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    fun openBottomSheet() {
        mBottomSheetBehavior?.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun setToggleMenuButtons() {
        binding.materialButtonTaskChooser.addOnButtonCheckedListener { _, checkedId, _ ->
            toggleButton(findViewById(checkedId))
        }
    }

    private fun toggleButton(button: MaterialButton) {
        val isColorWhite =
            button.textColors.defaultColor != ContextCompat.getColor(this, R.color.white)

        if (isColorWhite) {
            button.strokeColor = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.white))
            button.setTextColor(ContextCompat.getColor(this, R.color.white))
        } else {
            button.strokeColor =
                ColorStateList.valueOf(ContextCompat.getColor(this, R.color.pink))
            button.setTextColor(ContextCompat.getColor(this, R.color.pink))
        }
        when (button.id) {
            R.id.task1_button -> if (!isColorWhite) {
                binding.taskText.setText(R.string.task1_text)
            } else {
                binding.taskText.text = ""
            }
            R.id.task2_button -> if (!isColorWhite) {
                binding.taskText.setText(R.string.task2_text)
            } else {
                binding.taskText.text = ""
            }
            R.id.task3_button -> if (!isColorWhite) {
                binding.taskText.setText(R.string.task3_text)
            } else {
                binding.taskText.text = ""
            }
        }
    }

    private fun configureBackdrop() {
        val fragment = supportFragmentManager.findFragmentById(R.id.filter_fragment)

        (fragment?.view?.parent as View).let { view ->
            BottomSheetBehavior.from(view).let { bs ->

                bs.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                    override fun onSlide(bottomSheet: View, slideOffset: Float) {}
                    override fun onStateChanged(bottomSheet: View, newState: Int) {
                        listener?.onStateChanged(bottomSheet, newState)
                    }
                })

                bs.state = BottomSheetBehavior.STATE_EXPANDED

                mBottomSheetBehavior = bs
            }
        }
    }
}