package com.example.vociapp.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vociapp.data.local.database.Volunteer
import com.example.vociapp.data.repository.VolunteerRepository
import com.example.vociapp.data.util.Resource
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class VolunteerViewModel @Inject constructor(
    private val volunteerRepository: VolunteerRepository,
) : ViewModel() {

    private val _snackbarMessage = MutableStateFlow("")

    private val _specificVolunteer = MutableStateFlow<Resource<Volunteer?>>(Resource.Loading())
    val specificVolunteer: StateFlow<Resource<Volunteer?>> = _specificVolunteer.asStateFlow()

    private val firebaseAuth = FirebaseAuth.getInstance()

    private val _currentUser = MutableStateFlow<Volunteer?>(null)
    val currentUser: StateFlow<Volunteer?> = _currentUser.asStateFlow()

    init {
        fetchVolunteers()

        firebaseAuth.addAuthStateListener { auth ->
            auth.currentUser?.email?.let { email ->
                Log.d("AuthStateListener", "User is logged in: $email")
                viewModelScope.launch {
                    when (val volunteerIdResource = volunteerRepository.getVolunteerByEmail(email)) {
                        is Resource.Success -> {
                            val volunteerId = volunteerIdResource.data!!.id
                            val volunteer = volunteerRepository.getVolunteerById(volunteerId)
                            _currentUser.value =
                                if (volunteer is Resource.Success)
                                    volunteer.data
                                else
                                    null
                        }
                        is Resource.Error -> {
                            // Handle error fetching volunteer ID
                            Log.e("AuthStateListener", "Error fetching volunteer ID: ${volunteerIdResource.message}")
                        }
                        is Resource.Loading -> {
                            // Handle loading state if needed
                        }
                    }
                }
            } ?: run {
                _currentUser.value = null
            }
        }
    }

    //Ritorna il volontario connesso
    fun getCurrentUser(): Volunteer? {
        return _currentUser.value
    }

    fun getVolunteerById(id: String): StateFlow<Resource<Volunteer?>> {
        _specificVolunteer.value = Resource.Loading()
        viewModelScope.launch {
            _specificVolunteer.value = volunteerRepository.getVolunteerById(id)
        }
        return specificVolunteer
    }

    suspend fun checkIfNicknameExists(nickname: String): Boolean {
        val result = volunteerRepository.getVolunteerByNickname(nickname)
        return result is Resource.Success && result.data != null
    }

    suspend fun checkIfEmailExists(email: String): Boolean {
        return if(email.isEmpty()) false
        else volunteerRepository.getVolunteerByEmail(email).data != null
    }

    fun addVolunteer(volunteer: Volunteer) {
        viewModelScope.launch {
            val result = volunteerRepository.addVolunteer(volunteer)
            if (result is Resource.Success) {
                _snackbarMessage.value = "Registrazione effettuata"
                _currentUser.value = volunteer
            } else if (result is Resource.Error) {
                _snackbarMessage.value = "Errore durante la registrazione: ${result.message}"
            }
        }
    }

    fun updateVolunteer(volunteer: Volunteer) {
        viewModelScope.launch {
            when (val result = volunteerRepository.updateVolunteer(volunteer)) {
                is Resource.Success -> {
                    getVolunteerById(volunteer.id)
                }
                is Resource.Error -> {
                    println("Errore nella modifica dell'utente: ${result.message}")
                }
                is Resource.Loading -> println("Loading")
            }
        }
    }

    fun fetchVolunteers() {
        viewModelScope.launch {
            volunteerRepository.fetchVolunteersFromFirestoreToRoom()
        }
    }

    fun toggleHomelessPreference(homelessId: String) {
        viewModelScope.launch {
            val currentVolunteer = currentUser.value
            if (currentVolunteer != null) {
                val updatedPreferredIds =
                    if (homelessId in currentVolunteer.preferredHomelessIds) {
                        currentVolunteer.preferredHomelessIds - homelessId
                    } else {
                        currentVolunteer.preferredHomelessIds + homelessId
                    }
                volunteerRepository.updateVolunteer(currentVolunteer.copy(preferredHomelessIds = updatedPreferredIds))
                // Update _currentUser state to reflect the change
                _currentUser.value = currentVolunteer.copy(preferredHomelessIds = updatedPreferredIds)
            }
        }
    }
}