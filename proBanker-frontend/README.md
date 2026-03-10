# proBanker Frontend

A modern, responsive React-based frontend application for the proBanker banking system.

## Features

### Authentication
- User Registration with complete profile information
- Secure Login with OTP verification
- Password Reset functionality
- JWT token-based authentication

### Account Management
- Create and manage transaction PIN
- View account balance and details
- Update user profile information

### Transactions
- Cash Deposit
- Cash Withdrawal
- Fund Transfer between accounts
- Transaction history with filtering

### Dashboard
- Overview of account information
- Quick access to all banking operations
- Personal information display
- Balance summary

## Prerequisites

Before you begin, ensure you have the following installed:
- Node.js (v14 or higher)
- npm or yarn
- Your Spring Boot backend running (default: http://localhost:8080)

## Installation

1. Clone the repository or extract the project files

2. Navigate to the project directory:
```bash
cd proBanker-frontend
```

3. Install dependencies:
```bash
npm install
```

4. Create a `.env` file in the root directory:
```bash
cp .env.example .env
```

5. Update the `.env` file with your backend API URL:
```
REACT_APP_API_URL=http://localhost:8080/api
```

## Running the Application

### Development Mode

Start the development server:
```bash
npm start
```

The application will open in your browser at `http://localhost:3000`

### Production Build

Create an optimized production build:
```bash
npm run build
```

The build files will be in the `build` directory.

## Project Structure

```
proBanker-frontend/
├── public/
│   └── index.html
├── src/
│   ├── components/
│   │   └── PrivateRoute.js       # Protected route wrapper
│   ├── context/
│   │   └── AuthContext.js        # Authentication state management
│   ├── pages/
│   │   ├── Login.js              # Login page
│   │   ├── Register.js           # User registration
│   │   ├── ForgotPassword.js     # Password reset flow
│   │   ├── Dashboard.js          # Main dashboard
│   │   ├── CreatePin.js          # PIN management
│   │   ├── Deposit.js            # Cash deposit
│   │   ├── Withdraw.js           # Cash withdrawal
│   │   ├── Transfer.js           # Fund transfer
│   │   ├── Statement.js          # Transaction history
│   │   └── Profile.js            # User profile
│   ├── services/
│   │   └── api.js                # API service with axios
│   ├── styles/
│   │   ├── Auth.css              # Authentication pages styles
│   │   ├── Dashboard.css         # Dashboard styles
│   │   ├── Transaction.css       # Transaction pages styles
│   │   ├── Statement.css         # Statement page styles
│   │   └── Profile.css           # Profile page styles
│   ├── App.js                    # Main app component with routing
│   ├── index.js                  # Entry point
│   └── index.css                 # Global styles
├── package.json
├── .env.example
├── .gitignore
└── README.md
```

## API Endpoints Used

The frontend connects to the following backend endpoints:

### User APIs
- `POST /api/user/register` - User registration
- `POST /api/user/login` - User login
- `POST /api/user/generate-otp` - Generate OTP
- `POST /api/user/verify-otp` - Verify OTP
- `POST /api/user/update` - Update user profile
- `GET /api/user/logout` - User logout

### Authentication APIs
- `POST /api/authentication/password-reset/send-otp` - Send OTP for password reset
- `POST /api/authentication/password-reset/verify-otp` - Verify OTP for password reset
- `POST /api/authentication/password-reset` - Reset password

### Dashboard APIs
- `GET /api/dashboard/user` - Get user details
- `GET /api/dashboard/account` - Get account details

### Account APIs
- `GET /pin/check` - Check if PIN is created
- `POST /pin/create` - Create/update PIN
- `POST /deposit` - Cash deposit
- `POST /withdraw` - Cash withdrawal
- `POST /fundTransfer` - Fund transfer
- `GET /statement` - Get transaction history

## Features in Detail

### Security
- JWT token-based authentication
- Protected routes requiring authentication
- Automatic token refresh handling
- Secure PIN verification for transactions

### Responsive Design
- Mobile-first approach
- Works on all device sizes
- Touch-friendly interface
- Optimized for both desktop and mobile

### User Experience
- Loading states for all async operations
- Error handling with user-friendly messages
- Success notifications
- Form validation
- Intuitive navigation

## Deployment

### Deploy to Vercel

1. Install Vercel CLI:
```bash
npm install -g vercel
```

2. Deploy:
```bash
vercel
```

### Deploy to Netlify

1. Build the project:
```bash
npm run build
```

2. Deploy the `build` directory to Netlify

### Deploy to Firebase

1. Install Firebase CLI:
```bash
npm install -g firebase-tools
```

2. Initialize Firebase:
```bash
firebase init
```

3. Deploy:
```bash
firebase deploy
```

## Environment Variables

Create a `.env` file with the following variables:

```
REACT_APP_API_URL=http://localhost:8080/api
```

For production, update this to your production backend URL.

## Browser Support

- Chrome (latest)
- Firefox (latest)
- Safari (latest)
- Edge (latest)

## Contributing

1. Fork the repository
2. Create your feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## Troubleshooting

### CORS Issues
If you encounter CORS errors, ensure your Spring Boot backend has proper CORS configuration:

```java
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:3000")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowCredentials(true);
    }
}
```

### API Connection Issues
- Verify the backend is running
- Check the `REACT_APP_API_URL` in your `.env` file
- Ensure network connectivity

### Build Issues
If you encounter build errors:
```bash
rm -rf node_modules package-lock.json
npm install
npm start
```

## License

This project is part of the proBanker banking application.

## Support

For support, please contact your system administrator or raise an issue in the repository.
