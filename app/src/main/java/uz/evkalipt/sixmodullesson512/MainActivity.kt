package uz.evkalipt.sixmodullesson512

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.Navigation
import uz.evkalipt.sixmodullesson512.databinding.ActivityMainBinding
import uz.evkalipt.sixmodullesson512.utils.OnDataPass

class MainActivity : AppCompatActivity(), OnDataPass {

    var data1 = -1
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var navController = Navigation.findNavController(this, R.id.fragment)

        binding.bottomNavigation.setOnItemSelectedListener {

            when(it.itemId){
                R.id.home_menu -> {
                    navController.navigateUp()
                    navController.navigate(R.id.firstFragment)
                }
                R.id.love_menu -> {
                    navController.navigateUp()
                    navController.navigate(R.id.loveFragment)
                }
                R.id.info_menu -> {
                    navController.navigateUp()
                    navController.navigate(R.id.infoFragment)
                }
            }

            true
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        return Navigation.findNavController(this, R.id.fragment).navigateUp()
    }

    override fun onDataPass(data: Int) {
        data1 = data
        if (data1 == 1){
            binding.bottomNavigation.visibility = View.INVISIBLE
        }else if (data1 == 0){
            binding.bottomNavigation.visibility = View.VISIBLE
        }
    }
}