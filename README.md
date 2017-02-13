# MusicPlayer
뮤직 플레이어

## Permission
* READ_EXTERNAL_STORAGE : 음악 데이터는 외부저장소를 읽는 런타임 권한이 필요하다.
* manifest 에도 같이 정의해야만 한다.

## Content Resolver
```java
    // 1. 음악 데이터의 Content Uri
    Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

    // 2. 데이터 컬럼
    String proj[] = {
            MediaStore.Audio.Media._ID,     // 음원 아이디
            MediaStore.Audio.Media.ALBUM_ID,// 앨범 아이디 : 앨범이미지를 가져올때 사용
            MediaStore.Audio.Media.TITLE,   // 제목
            MediaStore.Audio.Media.ARTIST   // 가수
    };

```
## Media Player
```java
    // 1. 미디어 플레이어 사용하기
    MediaPlayer player = MediaPlayer.create(context, 음원Uri);

    // 2. 설정
    player.setLooping(false); // 반복여부

    // 3. 완료 리스너
    player.setOnCompletionListener(listener);

    // 4. 시작
    player.start();

    // 5. 일시정지
    player.pause();

    // 6. 종료
    player.stop();

    // 7. 해제
    player.release();
```


--------------------------------------------------



![](http://kubaspatny.github.io/assets/2014/09/18/animation.gif)
## **ViewPager**
>안드로이드 앱 중 좌우로 슬라이드하여 사용하는 액티비티가 있는 앱들이 있습니다. 이런 앱들에 사용되는 것들이 ViewFlipper나 ViewPager를 사용합니다. ViewFlipper의 경우 하나의 레이아웃에 뷰들이 많이 들어가게 되면 성능이 저하되는 점이 있어서 ViewPager를 많이 이용합니다.
>> 뷰페이저의 현재 위치를 나타내는 인디케이터나 자동으로 뷰페이저의 페이지가 넘어가게 하는 방법도 있습니다.

![](http://cfile25.uf.tistory.com/image/2550AC3855A1FEBB268954)
###**[Glide](https://github.com/bumptech/glide)**
> ####**유용한함수**
> -  override() :  지정한 이미지의 크기만큼만 불러올수 있습니다. 이를 통해 이미지 로딩 속도를 최적화 할수있습니다.
> - placeholder() : 이미지를 로딩하는동안 처음에 보여줄 placeholder이미지를 지정할 수 있습니다.
> - error() : 이미지로딩에 실패했을경우 실패 이미지를 지정할 수 있습니다.
> - thumbnail() :  지정한 %비율 만큼 미리 이미지를 가져와서 보여줍니다.
> 0.1f로 지정했다면 실제 이미지 크기중 10%만 먼저 가져와서 흐릿하게 보여줍니다.
>- asGif() : 정적인 이미지 뿐만 아니라 GIF도 로딩할수 있습니다.


###**SOLID**
* 객체지향 설계 5대 기본원리라고 할 수 있는 Solid 에 대해서 알아봅니다
* 5대 기본원리 또는 원칙이라고 알려져 있지만 항상 따라야 되는것은 아닙니다

## SRP (Single Responsibility Principle)
* 단일책임의 원리
```text
하나의 class 는 하나의 책임(하나의 object 특성)만 갖는다.
이는 method에도 동일하게 적용됩니다.
하나의 method 는 하나의 책임만 갖는다.
```

## OCP (Open Closed Principle)
* 개방폐쇄의 원리
```text
확장에 대해 열려(Open)야 하고, 수정에 대해서는 닫혀(Close)야 한다.
완성된 class 또는 method 는 수정하지 않습니다.(error는 제외)
error 이외에 기능의 추가 또는 수정시 extends를 통해 문제를 해결합니다.
```

## LSP (Liskov Substitution Principle)
* 리스코프 치환의 원리
```text
파생 class는 상위 class로 대체 가능해야 한다
상위 class의 기능을 파생 class가 포함해야 한다
상위 class의 기본기능을 파생 class 가 침범해서는 안된다
단, 파생 class에서 정의된 변수는 자체적으로 관리할 수 있다.
```

## ISP (Interface Segregation Principle)
* 인터페이스 분리의 원리
```text
특화된 여러개의 인터페이스가 범용 인터페이스 한개 보다 났다
ISP 또한 SRP 가 적용되야 한다. 하나의 Interface는 하나의 특성만 관리한다.
```

## DIP (Dependency Inversion Principle)
* 의존관계 역전의 원리
```text
추상화된것은 구체적인것에 의존하면 안된다.
변경될 소지가 있는 구현체라면 인터페이스에 의존해야 한다.
인터페이스를 받는 클라이언트 또한 구현체가 아닌 인터페이스에 의존해야한다.
```

