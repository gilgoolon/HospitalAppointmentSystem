function downloadAppointment(appId) {
    // redirect to appointment-download with id as parameter
    window.location.href = "appointment-download.xhtml?id=" + appId;
}

function cancelAppointment(appId) {
    console.log("hello world");
    //#{appointmentBean.onCancel(appId)};
}

function  cancelApp(appId) {
    console.log("Cancel app")
}