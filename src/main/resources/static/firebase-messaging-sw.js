// Service Worker pour Firebase Cloud Messaging

importScripts('https://www.gstatic.com/firebasejs/10.7.1/firebase-app-compat.js');
importScripts('https://www.gstatic.com/firebasejs/10.7.1/firebase-messaging-compat.js');

// ⚠️ REMPLACEZ PAR VOTRE CONFIGURATION
const firebaseConfig = {

};

firebase.initializeApp(firebaseConfig);

const messaging = firebase.messaging();

// Gérer les notifications en arrière-plan
messaging.onBackgroundMessage((payload) => {
    console.log('Notification en arrière-plan reçue:', payload);

    const notificationTitle = payload.notification.title;
    const notificationOptions = {
        body: payload.notification.body,
        icon: 'https://img.icons8.com/color/96/000000/notification.png'
    };

    self.registration.showNotification(notificationTitle, notificationOptions);
});
