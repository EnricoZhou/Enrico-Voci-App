package com.example.vociapp.ui.screens.profiles.userProfile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.vociapp.di.LocalServiceLocator
import com.example.vociapp.ui.navigation.Screens
import com.google.firebase.auth.FirebaseAuth

@Composable
fun UserProfileScreen(
    navController: NavHostController
) {
    val serviceLocator = LocalServiceLocator.current
    val authViewModel = serviceLocator.getAuthViewModel()
    val userProfile = authViewModel.getCurrentUserProfile()
    val volunteerViewModel = serviceLocator.getVolunteerViewModel()

    // NON serve più
    // val volunteerName = volunteerViewModel.getVolunteerName()

    // oggetto che contiene i dati del volontario corrente
    val CurrentVolunteer = volunteerViewModel.getCurrentVolunteer()

    val volunteerNickname = CurrentVolunteer?.nickname
    val volunteerName = CurrentVolunteer?.name
    val volunteerSurname = CurrentVolunteer?.surname
    val volunteerEmail = CurrentVolunteer?.email
    val volunteerPhoneNumber = CurrentVolunteer?.phone_number

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent)
            ) {
                Box(modifier = Modifier.fillMaxWidth().background(Color.Transparent)) {

                    // Edit button
                    IconButton(
                        onClick = { navController.navigate(Screens.UpdateUserProfile.route) },
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .size(38.dp),
                        colors = IconButtonColors(
                            containerColor = MaterialTheme.colorScheme.surface,
                            contentColor = MaterialTheme.colorScheme.primary,
                            disabledContainerColor = MaterialTheme.colorScheme.secondary,
                            disabledContentColor = MaterialTheme.colorScheme.onSecondary
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit Profile",
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier
                                .padding(6.dp)
                                .clip(CircleShape)
                                .size(40.dp)


                        )
                    }

                    // Logout button
                    IconButton(
                        onClick = { authViewModel.signOut() },
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .size(38.dp),
                        colors = IconButtonColors(
                            containerColor = MaterialTheme.colorScheme.surface,
                            contentColor = MaterialTheme.colorScheme.primary,
                            disabledContainerColor = MaterialTheme.colorScheme.secondary,
                            disabledContentColor = MaterialTheme.colorScheme.onSecondary
                        )
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = "Logout",
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier
                                .padding(6.dp)
                                .clip(CircleShape)
                                .size(40.dp)

                        )
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        userProfile?.let { profile ->

                            // Profile picture placeholder
                            Box(
                                modifier = Modifier
                                    .size(120.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.surface),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "Profile Picture",
                                    modifier = Modifier.size(64.dp),
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                            Text(
                                text = volunteerNickname?: "User",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold
                            )

                            // division line
                            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                            // Username (name + surname all in one line)
//                            ProfileInfoItem(
//                                icon = Icons.Default.Person,
//                                label = "Username",
//                                value = ("$volunteerName $volunteerSurname") ?: "Unknown Volunteer"
//                            )

                            // name
                            ProfileInfoItem(
                                icon = Icons.Default.Person,
                                label = "Name",
                                value = volunteerName ?: "Unknown Volunteer"
                            )

                            // surname
                            ProfileInfoItem(
                                icon = Icons.Default.Person,
                                label = "Surname",
                                value = volunteerSurname ?: "Unknown Volunteer"
                            )

                            // email
                            ProfileInfoItem(
                                icon = Icons.Default.Email,
                                label = "Email",
                                value = volunteerEmail ?: "Unknown Volunteer"
                            )

                            // phone number
                            ProfileInfoItem(
                                icon = Icons.Default.Phone,
                                label = "Phone Number",
                                value = volunteerPhoneNumber ?: "Unknown Volunteer"
                            )

                            // data di nascita
//                            ProfileInfoItem(
//                                icon = Icons.Default.CalendarMonth,
//                                label = "Data di Nascita",
//                                value = navController.currentBackStackEntry?.arguments?.getString("birth") ?: "Not set"
//                            )

                            // Edit Profile Section
                            Button(
                                onClick = { navController.navigate(Screens.UpdateUserProfile.route)},
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 16.dp),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("Edit Profile")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileInfoItem(icon: ImageVector, label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(text = label, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(text = value, style = MaterialTheme.typography.bodyLarge)
        }
    }
}