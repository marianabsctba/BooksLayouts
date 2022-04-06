package br.infnet.marianabs.layouts.ui.user

import android.annotation.SuppressLint
import android.app.UiModeManager.MODE_NIGHT_NO
import android.app.UiModeManager.MODE_NIGHT_YES
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import br.infnet.marianabs.layouts.MainActivity
import br.infnet.marianabs.layouts.R
import br.infnet.marianabs.layouts.ui.user.utils.Constants.Companion.DARKMOOD
import br.infnet.marianabs.layouts.ui.user.utils.Constants.Companion.EMAIL
import br.infnet.marianabs.layouts.ui.user.utils.Constants.Companion.NAME
import br.infnet.marianabs.layouts.ui.user.utils.Constants.Companion.PREFERENCE
import com.google.firebase.auth.FirebaseAuth
import br.infnet.marianabs.layouts.ui.viewmodel.ViewModelPattern
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@DelicateCoroutinesApi
class Settings : Fragment() {

    private lateinit var userNameET: EditText
    private lateinit var emailTV: TextView
    private lateinit var bookerChoices: SharedPreferences
    private lateinit var logOutTV: TextView
    private lateinit var editIconIV: ImageView
    private lateinit var doneIconIV: ImageView

    private val vm by lazy {
        ViewModelProvider(this)[ViewModelPattern::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater ,
        container: ViewGroup? ,
        savedInstanceState: Bundle? ,
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    @SuppressLint("WrongConstant", "UseSwitchCompatOrMaterialCode")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userNameET = view.findViewById(R.id.et_name)
        emailTV = view.findViewById(R.id.tv_email)
        logOutTV = view.findViewById(R.id.profile_signout)
        editIconIV = view.findViewById(R.id.editIcon)
        doneIconIV = view.findViewById(R.id.doneIcon)

        false.also { doneIconIV.isVisible = it }
        this.userNameET.isEnabled = false

        val getOptions = AnimationUtils.loadAnimation(context, R.anim.scale_up)

        bookerChoices = requireContext().getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)

        val userName = bookerChoices.getString(NAME, "")
        val userMail = bookerChoices.getString(EMAIL, "")

        emailTV.text = userMail
        userNameET.setText(userName)

        var clickInButton = false

        editIconIV.setOnClickListener {
            when {
                clickInButton -> {
                    GlobalScope.launch {
                        editIconIV.startAnimation(getOptions)
                        delay(500)
                        editIconIV.setImageResource(R.drawable.ic_edit)
                    }
                    userNameET.isEnabled = false
                    vm.refreshBookerName(userNameET.text.toString())
                    clickInButton = false

                }
                else -> {
                    GlobalScope.launch {
                        editIconIV.startAnimation(getOptions)
                        delay(500)
                        editIconIV.setImageResource(R.drawable.ic_baseline_done_24)
                    }
                    userNameET.isEnabled = true
                    clickInButton = true
                }
            }

        }

        val toggleLittle = view.findViewById<Switch>(R.id.switchTheme)
        toggleLittle.isChecked = bookerChoices.getBoolean(DARKMOOD, false)
        toggleLittle.setOnCheckedChangeListener { _ , isChecked ->
            when {
                ! isChecked -> {
                    AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO)
                    bookerChoices.edit().putBoolean(DARKMOOD, false).apply()
                }
                else -> {
                    AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES)
                    bookerChoices.edit().putBoolean(DARKMOOD, true).apply()

                }
            }
        }
        val mainActivity = requireActivity() as MainActivity

        logOutTV.setOnClickListener {
            GlobalScope.launch {
                logOutTV.startAnimation(getOptions)
                delay(400)

            }

            with(bookerChoices) { edit().clear().apply() }
            mainActivity.preferredUserMode(bookerChoices)
            FirebaseAuth.getInstance().signOut()
            this.findNavController().navigate(R.id.action_profileFragment_to_loginFragment2)

        }
    }

    private fun refreshCurrentFragment() {
        val id: Int?
        id = findNavController().currentDestination?.id
        findNavController().navigateUp()
        findNavController().navigate(id!!)
    }
}