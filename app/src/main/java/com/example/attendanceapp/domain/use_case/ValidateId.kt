package com.example.attendanceapp.domain.use_case

import com.example.attendanceapp.core.utils.OperationStatus

object ValidateId {

    fun validateDbId(id: Int): Boolean{
        return  id > 0
    }

    fun validateListIndex(id: Int): Boolean{
        return  id > -1
    }

}

object ValidateEventId {
     fun validateEventDId(id: Int): OperationStatus<Boolean>{
        //if (!ValidateId.validateDbId(id))
             return OperationStatus.Error(message = "Invalid id")

     }

 }


