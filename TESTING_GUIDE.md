# Complete Testing Scenarios for CampusLost

## Scenario 1: New User Registration and Item Posting

### Step 1: Register New User
1. Open http://localhost:3000/index.html (or your frontend URL)
2. Click "Create Account"
3. Fill form:
   - Register No: 22CSE1001
   - Name: Test User
   - Department: CSE
   - College: Test College
   - Email: test@example.com
   - Password: password123
4. Click "Sign Up"
5. Verify: Success message appears
6. Verify: Redirected to login

### Step 2: Login
1. Enter email: test@example.com
2. Enter password: password123
3. Click "Login"
4. Verify: Success message and redirected to main app

### Step 3: Post Lost Item
1. Navigate to post.html
2. Fill form:
   - Register No: 22CSE1002
   - Name: Jane Doe
   - Department: ECE
   - College: Test College
   - Email: jane@example.com (optional)
3. Click "Post Lost Item"
4. Verify: Success message appears

### Step 4: Report Found Item
1. Navigate to found.html
2. Fill form:
   - Register No: 22CSE1002
   - Name: Jane Doe
   - Notes: Found near library entrance
3. Click "Submit Found Report"
4. Verify: Success message and potential matches shown

## Scenario 2: Search and Filter Testing

### Step 1: Create Multiple Items
Use the HTTP client to create several items with different:
- Categories (Electronics, Documents, Accessories)
- Statuses (LOST, FOUND)
- Keywords in titles/descriptions

### Step 2: Test Search Functionality
1. Open main dashboard (if available in your frontend)
2. Try searching for keywords
3. Test category filters
4. Test status filters

## Scenario 3: Error Handling Testing

### Step 1: Test Invalid Registration
1. Try registering with existing email
2. Verify: Proper error message shown

### Step 2: Test Invalid Login
1. Try login with wrong password
2. Verify: Proper error message shown

### Step 3: Test Unauthorized Access
1. Try posting item without login
2. Verify: Proper authentication error

## Scenario 4: Database Verification

### Step 1: Check H2 Database
1. Open http://localhost:8081/h2-console
2. Connect with credentials
3. Run queries:
   ```sql
   SELECT * FROM users;
   SELECT * FROM items;
   SELECT i.title, i.status, u.name as user_name 
   FROM items i JOIN users u ON i.user_id = u.id;
   ```

## Expected Results Checklist

### Authentication:
- [ ] User registration works
- [ ] User login works
- [ ] JWT token stored in localStorage
- [ ] Protected routes require authentication

### Item Management:
- [ ] Items are saved to database
- [ ] Items appear in API responses
- [ ] Search functionality works
- [ ] Filtering works
- [ ] User can only edit their own items

### Integration:
- [ ] Frontend calls backend APIs
- [ ] Database stores data persistently
- [ ] Real-time search/matching works
- [ ] Error messages display properly

## Troubleshooting Common Issues

### Backend Issues:
- Port 8081 in use: Change server.port in application.properties
- Database errors: Check H2 console for table structure
- Authentication failures: Verify JWT secret key

### Frontend Issues:
- CORS errors: Ensure @CrossOrigin annotation in controllers
- Network errors: Check if backend is running on port 8081
- Authentication issues: Verify JWT token in localStorage

### Integration Issues:
- API calls failing: Check browser network tab for error details
- Data not persisting: Verify database connections
- Search not working: Check repository query methods
