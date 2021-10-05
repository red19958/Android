package com.example.hw3_contacts

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView


class MainActivity : AppCompatActivity() {
    private val myRequestId = 11
    private var contactList = ArrayList<Contact>()
    private var size = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkPermission()
    }

    override fun onResume() {
        super.onResume()
        if(size != 0){
            Toast.makeText(
                applicationContext, resources.getQuantityString(
                    R.plurals.contacts_loaded,
                    size,
                    size
                ), Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(applicationContext,
                Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this@MainActivity,
                arrayOf(Manifest.permission.READ_CONTACTS),
                myRequestId)
        } else {
            val set = fetchAllContacts()
            size = set.size
            val myRecyclerView = findViewById<RecyclerView>(R.id.recyclerView)
            myRecyclerView.adapter = ContactAdapter(this, set)
        }
    }

    private fun processReceivedPermission() {
        contactList.clear()
        contactList.addAll(fetchAllContacts())
        Toast.makeText(
            this@MainActivity,
            R.string.closeSMS,
            Toast.LENGTH_SHORT
        ).show()

        /*Toast.makeText(
            applicationContext, resources.getQuantityString(
                R.plurals.contacts_loaded,
                contactList.size,
                contactList.size
            ), Toast.LENGTH_LONG
        ).show()*/
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            myRequestId -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    processReceivedPermission()
                } else {
                    Toast.makeText(
                        applicationContext,
                        R.string.permission_not_received,
                        Toast.LENGTH_LONG
                    ).show()
                }
                return
            }
        }
    }


    data class Contact(val personName: String, val phoneNumber: String)

    class ContactViewHolder(root: View) : RecyclerView.ViewHolder(root) {
        val personName: TextView = root.findViewById(R.id.personName)
        val phoneNumber: TextView = root.findViewById(R.id.phoneNumber)
        val button: Button = root.findViewById(R.id.button)
    }

    class ContactAdapter(
        private val context: Context,
        private val contacts: List<Contact>,
    ) : RecyclerView.Adapter<ContactViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
            return ContactViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item, parent, false))
        }

        override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
            val current = contacts[position]
            with(holder) {
                personName.text = current.personName
                phoneNumber.text = current.phoneNumber
                personName.setOnClickListener {
                    context.startActivity(
                        Intent(
                            Intent.ACTION_DIAL,
                            Uri.parse("tel:${current.phoneNumber}")
                        )
                    )
                }

                phoneNumber.setOnClickListener {
                    context.startActivity(
                        Intent(
                            Intent.ACTION_DIAL,
                            Uri.parse("tel:${current.phoneNumber}")
                        )
                    )
                }

                button.setOnClickListener {
                    val sendIntent = Intent(Intent.ACTION_SENDTO)
                    sendIntent.data = Uri.parse("smsto:${current.phoneNumber}")
                    sendIntent.putExtra("sms_body", context.getString(R.string.defaultMessage))
                    context.startActivity(sendIntent)
                }
            }
        }

        override fun getItemCount() = contacts.size
    }

    private fun Context.fetchAllContacts(): List<Contact> {
        contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            null)
            .use { cursor ->
                if (cursor == null) return emptyList()
                val builder = ArrayList<Contact>()
                while (cursor.moveToNext()) {
                    val name =
                        cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                            ?: "N/A"
                    val phoneNumber =
                        cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                            ?: "N/A"

                    builder.add(Contact(name, phoneNumber))
                }
                return builder
            }
    }
}