package br.infnet.marianabs.layouts.ui.user.authentication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import br.infnet.marianabs.layouts.R
import com.google.firebase.auth.FirebaseAuth


class resetPassword : Fragment() {

    private lateinit var reset: Button
    private lateinit var email: EditText
    private lateinit var returning: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater ,
        container: ViewGroup? ,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_forget_password,
        container,
        false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        reset = view.findViewById(R.id.btnResetPass)
        email = view.findViewById(R.id.et_email)
        returning = view.findViewById(R.id.arrow_back)

        returning.setOnClickListener{
            findNavController().navigate(R.id.action_forgetPassword_to_loginFragment2)

        }
        reset.setOnClickListener{

            val email = email.text.toString().trim { it <= ' ' }
            when {
                email.isNotEmpty() -> {
                    FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(
                                    context,
                                    "Recuperação de senha enviada ao seu e-mail ",
                                    Toast.LENGTH_LONG
                                ).show()
                                findNavController().navigate(R.id.action_forgetPassword_to_loginFragment2)

                            } else {
                                Toast.makeText(
                                    context,
                                    task.exception!!.message.toString(),
                                    Toast.LENGTH_LONG
                                )
                                    .show()
                            }
                        }
                }
                else -> {
                    Toast.makeText(context, "Insira seu e-mail.... ", Toast.LENGTH_LONG).show()

                }
            }
        }
    }
}