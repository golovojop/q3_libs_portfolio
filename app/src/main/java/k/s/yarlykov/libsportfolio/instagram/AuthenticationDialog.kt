package k.s.yarlykov.libsportfolio.instagram

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import k.s.yarlykov.libsportfolio.R

class AuthenticationDialog(val ctx : Context) : Dialog(ctx) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_auth_dialog)
    }

}