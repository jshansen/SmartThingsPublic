definition(
    name: "When I Arrive",
    namespace: "drandyhaas",
    author: "SmartThings/AndyHaas",
    description: "Unlocks the door and disarms security when you arrive at your location.",
    category: "Safety & Security",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience%402x.png",
    oauth: true
)

preferences {
	section("When I arrive..."){
		input "presence1", "capability.presenceSensor", title: "Who?", multiple: true
	}
	section("Unlock the lock..."){
		input "lock1", "capability.lock", multiple: true
	}
}

def installed()
{
	subscribe(presence1, "presence.present", presence)
}

def updated()
{
	unsubscribe()
	subscribe(presence1, "presence.present", presence)
}

def presence(evt)
{
    log.info "presence detected..."
	def anyLocked = lock1.count{it.currentLock == "unlocked"} != lock1.size()
	if (anyLocked) {
		sendPush "Unlocked door due to arrival of $evt.displayName"
		lock1.unlock()
	}    
    //sendLocationEvent(name: "alarmSystemStatus", value: "away")
    //sendLocationEvent(name: "alarmSystemStatus", value: "stay")
    sendLocationEvent(name: "alarmSystemStatus", value: "off")
}