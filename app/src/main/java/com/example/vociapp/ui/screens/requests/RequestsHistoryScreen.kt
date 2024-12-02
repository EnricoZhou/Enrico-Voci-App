package com.example.vociapp.ui.screens.requests

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.vociapp.data.types.RequestStatus
import com.example.vociapp.data.util.Resource
import com.example.vociapp.data.util.SortOption
import com.example.vociapp.di.LocalServiceLocator
import com.example.vociapp.ui.components.RequestCard
import com.example.vociapp.ui.components.SortButtons

@Composable
fun RequestsHistoryScreen() {

    val serviceLocator = LocalServiceLocator.current
    val requestViewModel = serviceLocator.getRequestViewModel()
    val requests by requestViewModel.requests.collectAsState()
    var doneRequests = requests.data.orEmpty().filter { it.status == RequestStatus.DONE }

    val sortOptions = listOf(
        SortOption("Latest") { r1, r2 -> r2.timestamp.compareTo(r1.timestamp) },
        SortOption("Oldest") { r1, r2 -> r1.timestamp.compareTo(r2.timestamp) }
    )
    var selectedSortOption by remember { mutableStateOf(sortOptions[0]) }

    LaunchedEffect(Unit) {
        requestViewModel.getRequests()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        SortButtons(
            sortOptions = sortOptions,
            selectedSortOption = selectedSortOption,
            onSortOptionSelected = { selectedSortOption = it }
        )

        Column {



            Box(modifier = Modifier.fillMaxSize()) {

                when (requests) {
                    is Resource.Loading -> {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }
                    is Resource.Success -> {
                        if (doneRequests.isEmpty()) {
                            Text("Non ci sono richieste completate", modifier = Modifier.align(
                                Alignment.Center))
                        } else {
                            doneRequests = doneRequests.sortedWith(selectedSortOption.comparator)
                            LazyColumn {
                                items(doneRequests) { request ->
                                    RequestCard(request = request)
                                }
                            }
                        }
                    }
                    is Resource.Error -> {
                        Text(
                            text = "Error: ${requests.message}",
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
            }
        }

    }

}