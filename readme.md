sequenceDiagram
autonumber

    actor User
    participant API as libraryhub-api
    participant CIR as libraryhub-circulation
    participant CAT as libraryhub-catalog
    participant PAY as libraryhub-payment
    participant DB as Database

    User ->> API: POST /api/borrow
    API ->> CIR: borrowBook(userId, bookId)

    CIR ->> CAT: validateAvailability(bookId)
    CAT ->> DB: findBookCopy(bookId)
    DB -->> CAT: available
    CAT -->> CIR: OK

    CIR ->> DB: save Loan
    DB -->> CIR: Loan saved

    CIR ->> PAY: createFineIfRequired(loan)
    PAY ->> DB: save Payment (if any)
    DB -->> PAY: Payment saved
    PAY -->> CIR: paymentStatus

    CIR -->> API: BorrowResult
    API -->> User: 200 OK (Borrow successful)


Reservation State Diagram

stateDiagram-v2
[*] --> ACTIVE
ACTIVE --> READY_FOR_PICKUP : Copy available
READY_FOR_PICKUP --> FULFILLED : Loan created
READY_FOR_PICKUP --> EXPIRED : Not picked before expiry
ACTIVE --> CANCELLED : User cancels










Reservation Flow

User creates reservation
↓
Status = ACTIVE
↓
Book copy becomes available?
↓ YES
Status = READY_FOR_PICKUP
Expiry date set
↓
User picks up before expiry?
↓ YES
Create Loan
Status = FULFILLED
↓ NO
Status = EXPIRED
Copy → AVAILABLE





LOAN WORKFLOW
Loan Status States

ACTIVE
OVERDUE
RETURNED
LOST



Loan Lifecycle
Loan Created
Status = ACTIVE
↓
Due date passed?
↓ YES
Status = OVERDUE
↓
Book Returned?
↓ YES
Calculate fine (if overdue)
Status = RETURNED




Loan State Diagram
stateDiagram-v2
[*] --> ACTIVE
ACTIVE --> OVERDUE : Due date passed
ACTIVE --> RETURNED : Returned on time
OVERDUE --> RETURNED : Returned late
ACTIVE --> LOST : Marked lost


. FULL SYSTEM FLOW (Reservation → Loan → Fine)
User
│
├── Create Reservation
│        │
│        ▼
│   ACTIVE → READY_FOR_PICKUP
│        │
│        ▼
│   Librarian creates Loan
│        │
▼        ▼
Loan ACTIVE → OVERDUE?
│
▼
Returned
│
▼
Fine Generated?
│
▼
Fine Paid









Production-Grade Sequence Diagram
sequenceDiagram
participant User
participant System
participant Librarian
participant DB

    User->>System: Create Reservation
    System->>DB: Insert reservation (ACTIVE)

    System->>DB: Detect available copy
    System->>DB: Update reservation READY_FOR_PICKUP

    User->>Librarian: Pick up book
    Librarian->>System: Create loan
    System->>DB: Insert loan
    System->>DB: Update copy LOANED
    System->>DB: Update reservation FULFILLED

    User->>Librarian: Return book
    Librarian->>System: Process return
    System->>DB: Update loan RETURNED
    System->>DB: Insert fine (if overdue)







Automated System (Schedulers / Background Jobs)

Mark Overdue Loans

Daily job (cron) → update loans where due_date < today and return_date IS NULL → status = OVERDUE

Expire Reservations

Hourly/daily job → update reservations where expiry_date < today → status = EXPIRED

Send Notifications / Reminders

Scheduled push emails/SMS for:

Reservations ready for pickup

Loans due tomorrow

Overdue loans

Optional Reports / Analytics

Daily/weekly job → update metrics, usage reports, branch statistics



USER / MEMBER
│
├─ Create Reservation API → Reservation Table (status=ACTIVE)
│
├─ Cancel Reservation API → Reservation Table (status=CANCELLED)
│
├─ Borrow Book API → Loan Table (status=ACTIVE)
│
└─ Return Book API → Loan Table (status=RETURNED)
│
└─ Fine Creation (if overdue) → Fine Table (status=UNPAID)

SCHEDULERS / BACKGROUND JOBS
│
├─ Daily: Mark Overdue Loans → Loan Table (status=OVERDUE)
├─ Hourly/Daily: Expire Reservations → Reservation Table (status=EXPIRED)
├─ Daily: Send Notifications → Email/SMS
└─ Optional: Analytics / Reports → Dashboard



+----------------+          +----------------+
|     User       |          |  LibraryBranch |
|----------------|          |----------------|
| user_id        |          | branch_id      |
| name           |          | name           |
+----------------+          +----------------+
|                           |
| makes                     | owns
v                           v
+----------------+          +----------------+
|  Reservation   |<-------->|      Book      |
|----------------|          |----------------|
| reservation_id |          | book_id        |
| reservation_date|         | title          |
| expiry_date    |          +----------------+
| status         |                 ^
| priority       |                 |
+----------------+                 |
|                            |
| expires (scheduler)        |
v                            |
+----------------+                  |
| Loan           |<-----------------+
|----------------|
| loan_id        |
| issue_date     |
| due_date       |
| return_date    |
| status         |
| renewal_count  |
+----------------+
|
| generates on return
v
+----------------+
| Fine           |
|----------------|
| fine_id        |
| amount         |
| status         |
| paid_date      |
+----------------+

Schedulers:
-------------
1. Daily mark overdue loans → updates Loan.status = OVERDUE
2. Hourly/daily expire reservations → updates Reservation.status = EXPIRED
3. Daily send reminders → notifications for due/overdue books or ready reservations

APIs:
-----
1. POST /api/v1/reservations → Create reservation
2. PATCH /api/v1/reservations/{id}/cancel → Cancel reservation
3. POST /api/v1/loans → Create loan
4. PATCH /api/v1/loans/{id}/return → Return book + fine creation