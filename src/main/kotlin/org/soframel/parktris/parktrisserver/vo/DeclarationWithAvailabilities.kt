package org.soframel.parktris.parktrisserver.vo

class DeclarationWithAvailabilities: FreeSlotDeclaration {

    constructor(parent: FreeSlotDeclaration){
        this.startDate=parent.startDate
        this.endDate=parent.endDate
        this.preferedTenant=parent.preferedTenant
        this.id=parent.id
        this.owner=parent.owner
        this.slotId=parent.slotId
    }

    var availabilities: List<DateInterval> = emptyList()
}