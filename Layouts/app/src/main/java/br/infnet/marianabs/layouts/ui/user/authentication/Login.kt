package br.infnet.marianabs.layouts.ui.user.authentication

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import br.infnet.marianabs.layouts.R
import br.infnet.marianabs.layouts.ui.user.utils.Constants.Companion.CHECKBOX
import br.infnet.marianabs.layouts.ui.user.utils.Constants.Companion.EMAIL
import br.infnet.marianabs.layouts.ui.user.utils.Constants.Companion.PASSWORD
import br.infnet.marianabs.layouts.ui.user.utils.Constants.Companion.PREFERENCE
import br.infnet.marianabs.layouts.ui.user.utils.Validation
import br.infnet.marianabs.layouts.ui.viewmodel.ViewModelPattern
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment() {

    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var tvSignup: TextView
    private lateinit var forgetPassword: TextView
    private lateinit var btnLogin: Button
    private lateinit var rememberMe: CheckBox
    private lateinit var preferences: SharedPreferences

    var getAlerts = false
    private val viewModelStyle by lazy {
        return@lazy ViewModelProvider(this)[ViewModelPattern::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater ,
        container: ViewGroup? ,
        savedInstanceState: Bundle? ,
    ): View? = inflater.inflate(R.layout.fragment_login , container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.preferences = requireContext().getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)
        val editing: SharedPreferences.Editor = preferences.edit()


        this.getAlerts = preferences.getBoolean(CHECKBOX, false)
        if (getAlerts) findNavController().navigate(R.id.action_loginFragment2_to_bookFragment)

        email = view.findViewById(R.id.et_email)
        password = view.findViewById(R.id.et_password)
        btnLogin = view.findViewById(R.id.btn_login)
        tvSignup = view.findViewById(R.id.tv_sign_up)
        forgetPassword = view.findViewById(R.id.tv_forget_password)
        rememberMe = view.findViewById(R.id.checkBox_remember)

        this.forgetPassword.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment2_to_forgetPassword)
        }

        this.tvSignup.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment2_to_signUpFragment2)
        }
        this.btnLogin.setOnClickListener {

            when {
                TextUtils.isEmpty(email.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        context,
                        getString(R.string.please_enter_email),
                        Toast.LENGTH_LONG
                    ).show()
                }
                TextUtils.isEmpty(password.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        context,
                        getString(R.string.please_enter_password),
                        Toast.LENGTH_LONG
                    ).show()

                }
                ! Validation.email(email.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        context,
                        getString(R.string.invalid_email),
                        Toast.LENGTH_LONG
                    ).show()

                }
                else -> {
                    val email: String = email.text.toString().trim { it <= ' ' }
                    val password: String = password.text.toString().trim { it <= ' ' }

                    FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->

                            if (! task.isSuccessful) {
                                Toast.makeText(
                                    context,
                                    getString(R.string.try_again),
                                    Toast.LENGTH_LONG
                                ).show()
                            } else {
                                viewModelStyle.userData(viewLifecycleOwner)

                                editing.putString(EMAIL, email)
                                    .putString(PASSWORD, password)
                                    .putBoolean(CHECKBOX, rememberMe.isChecked)
                                    .apply()

                                this.findNavController().navigate(R.id.action_loginFragment2_to_bookFragment)

                            }
                        }
                }
            }
        }
    }
}