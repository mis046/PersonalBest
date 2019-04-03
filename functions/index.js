const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();

exports.addTimeStamp = functions.firestore
    .document('chats/{userId}/people/{friendId}/message/{messageId}')
    .onCreate((snap, context) => {
      if (snap) {
        return snap.ref.update({
                    timestamp: admin.firestore.FieldValue.serverTimestamp()
                });
      }

      return "snap was null or empty";
    });

exports.sendChatNotifications = functions.firestore
   .document('chats/{userId}/people/{friendId}/message/{messageId}')
   .onCreate((snap, context) => {
     // Get an object with the current document value.
     // If the document does not exist, it has been deleted.
     const document = snap.exists ? snap.data() : null;

     if (document) {
       var message = {
         notification: {
           title: document.from + ' sent you a message',
           body: document.text
         },
         "data": {
            "click_action": "OPEN_CHAT",
            "friendEmail": document.from
         },
         topic: context.params.userId
       };

       return admin.messaging().send(message)
         .then((response) => {
           // Response is a message ID string.
           console.log('Successfully sent message:', response);
           return response;
         })
         .catch((error) => {
           console.log('Error sending message:', error);
           return error;
         });
     }

     return "document was null or emtpy";
   });

exports.pushGoalNotification = functions.firestore
   .document('friends/friendActivity/{userID}/{dateID}')
   .onWrite((change, context) => {
     // Get an object with the current document value.
     // If the document does not exist, it has been deleted.
     const documentPush = change.after.exists ? change.after.data() : null;

     console.log('Successfully triggered push goal notification');

     if (documentPush) {
       var message = {
         notification: {
           title: 'You have reached your goal! ',
           body: 'current goal: ' + documentPush.goal + ', your steps: ' + documentPush.totalStep
         },
          "data": {
            "click_action": "OPEN_GOAL",
          },
         topic: context.params.userID
       };

       if (documentPush.totalStep >= documentPush.goal) {
           return admin.messaging().send(message)
             .then((response) => {
               // Response is a message ID string.
               console.log('Successfully sent message:', response);
               return response;
             })
             .catch((error) => {
               console.log('Error sending message:', error);
               return error;
             });
       }
     } else {
          console.log('Document is null');
     }


     return "document was null or emtpy";
   });
