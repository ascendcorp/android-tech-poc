# android-tech-poc

POC any new technologies to be applied into the real project

## Features

### Home design with MotionLayout

- Basic MotionLayout
  - Using MotionLayout to support Home's swipe gestures.
  - <img src="https://user-images.githubusercontent.com/51980713/192190225-34057072-b588-4296-a1a2-6d44f4350d2c.gif" width="250">
- Home MotionLayout
  - Using Basic MotionLayout with RecyclerView.
  - <img src="https://user-images.githubusercontent.com/51980713/192190324-43e9d4d0-714c-4ba6-860f-9e2eae54a224.gif" width="250">
- Home
  - Using Home MotionLayout with ViewPager2.
  - <img src="https://user-images.githubusercontent.com/51980713/192190377-672133b8-fea9-4bba-9c8a-d41a81f6d9d1.gif" width="250">

### Navigation Component

- Navigate to a destination
  - Navigation component support several ways for navigating to destination. To explore each solution, change `ActionType` at `NavGraphActivity`, `NavGraphHomeAFragment`, or `NavGraphHomeCSubFragment`.
  - <img src="https://user-images.githubusercontent.com/51980713/178474051-1b142b93-097b-43ad-8869-5fc06392b7ca.gif" width="250">
- Deep link
  - Deep linking to specific destination has two different types of deep link. Check on `ActionType.DEEP_LINK`, `ActionType.MAIN_NAVIGATOR_DEEP_LINK` at `NavGraphHomeAFragment` for each type.
  - Available deep links
    - Home A: `androidtechpoc://navgraph/homeA`
    - Home B: `androidtechpoc://navgraph/homeB`
    - Home C: `androidtechpoc://navgraph/homeC`
    - Home C-sub: `androidtechpoc://navgraph/homeCsub`
    - Home D: `androidtechpoc://navgraph/homeD/{displayText}`
  - <img src="https://user-images.githubusercontent.com/51980713/178474070-d422101e-4457-4db8-8899-532f6ec8fc7f.gif" width="250">

### Reverse Engineering

- A learning resource for the basic reverse engineering topic
    - Provide a sample application for anyone who are interesting in reverse engineering to explore
      with some instructions.<br />
      <img src="https://user-images.githubusercontent.com/44750404/191717595-24af34d2-291d-4545-8680-56f580ec6234.jpg" width="250">

## Source

### Images

- Use images from https://www.flaticon.com/
