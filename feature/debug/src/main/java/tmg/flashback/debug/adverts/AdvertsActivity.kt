package tmg.flashback.debug.adverts

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import tmg.flashback.debug.databinding.ActivityAdvertsBinding

class AdvertsActivity: AppCompatActivity() {

    private lateinit var binding: ActivityAdvertsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdvertsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}