/// Get your own App ID at https://dashboard.agora.io/
String get appId {
  // Allow pass an `appId` as an environment variable with name `TEST_APP_ID` by using --dart-define
  return const String.fromEnvironment('TEST_APP_ID',
      defaultValue: '79daa6d2970d4978a08c21915fbfc1d9');
}

/// Please refer to https://docs.agora.io/en/Agora%20Platform/token
String get token {
  // Allow pass a `token` as an environment variable with name `TEST_TOEKN` by using --dart-define
  return const String.fromEnvironment('TEST_TOEKN',
      defaultValue: '00679daa6d2970d4978a08c21915fbfc1d9IACeQECeARppKmELt4J+GSAF03ahI4XcEE+F+7TQnvM0pgx+f9gAAAAAEADjTvSOvQozYgEAAQC9CjNi');
}

/// Your channel ID
String get channelId {
  // Allow pass a `channelId` as an environment variable with name `TEST_CHANNEL_ID` by using --dart-define
  return const String.fromEnvironment(
    'TEST_CHANNEL_ID',
    defaultValue: 'test',
  );
}

/// Your int user ID
const int uid = 0;

/// Your user ID for the screen sharing
const int screenSharingUid = 10;

/// Your string user ID
const String stringUid = '0';
