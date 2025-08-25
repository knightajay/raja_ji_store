package com.example.partnerji

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.partnerji.databinding.ActivitySignInBinding
import com.example.partnerji.model.usermodel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database

class SignInActivity : AppCompatActivity() {
    private lateinit var email:String
    private lateinit var password :String
    private lateinit var username:String
    private lateinit var address :String
    private lateinit var nameofcompany :String
    private lateinit var auth : FirebaseAuth
    private lateinit var database : DatabaseReference
    private lateinit var googleSignInclient : GoogleSignInClient
    private val binding: ActivitySignInBinding by lazy {
        ActivitySignInBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val googleSignInOptions= GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken (getString(R.string.default_web_client_id)).requestEmail().build()


//Firebase Autuh
        auth= Firebase.auth
        //intailize databse
        database= Firebase.database.reference
//intialize firebase database
        googleSignInclient= GoogleSignIn.getClient(this,googleSignInOptions)
        binding.allreadyhaveacount.setOnClickListener {
            val intent= Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }

        binding.createUserButton.setOnClickListener{
            username=binding.name.text.toString()
            email=binding.emailorphone.text.toString().trim()
            password=binding.password.text.toString().trim()
            address=binding.adress.text.toString().trim()
            nameofcompany=binding.nameofcompany.text.toString().trim()
            if(email.isBlank()||password.isBlank()||username.isBlank() ||address.isBlank()||nameofcompany.isBlank()){
                Toast.makeText(this,"Please Fill all the detail", Toast.LENGTH_SHORT).show()
            }else{
                creaAccount(email,password)
            }
        }
        binding.google.setOnClickListener{
            val signIntent=googleSignInclient.signInIntent
            launcher.launch(signIntent)
        }
    }
    private val launcher=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result->
        if(result.resultCode== Activity.RESULT_OK){
            val task= GoogleSignIn.getSignedInAccountFromIntent(result.data)
            if(task.isSuccessful){
                val account: GoogleSignInAccount? =task.result
                val credential: AuthCredential =
                    GoogleAuthProvider.getCredential(account?.idToken,null)
                auth.signInWithCredential(credential).addOnCompleteListener{
                        task->
                    if(task.isSuccessful){
                        Toast.makeText(this,"Sign In Succesfully", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this,MainActivity::class.java))
                        finish()
                    }else{
                        Toast.makeText(this,"Sign IN Failed" , Toast.LENGTH_SHORT).show()
                    }
                }}
            else{
                Toast.makeText(this,"Sign IN Failedd" , Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun creaAccount(email:String,password:String) {
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener{
                task->
            if(task.isSuccessful){
                Toast.makeText(this,"Account Created Sucessfully" , Toast.LENGTH_SHORT).show()
//                val intent=Intent(this,LoginActivity::class.java)
//                startActivity(intent)
                saveUserData()
                startActivity(Intent(this,LoginActivity::class.java))
                finish()
            }else{
                Toast.makeText(this, "Account Creation is Filed", Toast.LENGTH_SHORT).show()
                Log.d("Account","CreateAccount :Failure",task.exception)
            }
        }
    }

    private fun saveUserData() {
        //retrive daata from inpput field
        username=binding.name.text.toString()
        email=binding.emailorphone.text.toString().trim()
        password=binding.password.text.toString().trim()
        val user=usermodel(username,email,password)
        val userId= FirebaseAuth.getInstance().currentUser!!.uid
        //save fata base in firebase

        database.child("Admin").child(userId).setValue(user)
    }
}