# VGMovie
猫眼电影_大概框架
自定义属性步骤:(以继承自View 为例)类属布得画
1_创建工程： 建议UTF-8编码
2_创建属性类:MyAttributeView继承自：View
3_创建工程的属性文件在Values 工程文件下建：attrs.xml，包含常见属性类型。[如不会建，参考查看View的属性E:\Android_source\2.3源码
\JB\frameworks\base\core\res\res\values\attrs.xml]
4_使用自定义类并和自定义属性: 在当前MainActivity 的布局文件里把当前的MyAttributeView 类的全类名拷到想自定义的东西上去。
5_三种方式得到属性值：（得到属性也叫取值，不取值什么也不显示：原因是定义了一个类，只是把他放到MainActivity 的布局文件去了，那以以往的经验来看，凡是用类，都是必须要实例化的。
在布局文件里做的事情只是通过反射机制，进行pull解析，把当前类的属性集合放到自定义类的构造方法中的属性集合中去，但是在类里没有任何获取的话，理所应当的什么也没有得到xml文件里的实参属性。所以要在类里获取[在类里写代码获取]）
// 三种方式从AttributeSet取对应的属性：还是用系统的比较好，可以得到的类型比较全，比如能得到图片。
// 1.用命名空间取对应的属性- String
就是自动生成的那句话。  
// 2.遍历方式取属性String 和int
// 3.用系统工具TypedArray取属性- 取各种值Bitmap String 等等
      
6_把取到的属性值给给画出来：
在MyAttributeView 中重写onDraw 进行绘制。
数据的流动的过程：MainActivity的xml 文件中的实参---传到到自定义的类的属性集合attrs---实参数据再传到value 文件夹下的attrs文件或其他文件---实参数据再从attrs 文件夹传到自动生成的r 文件中，最终实现绘制。




 一切开始于MainActivity 的私有属性，有四个，就是底部对应的那些东西
中间省略若干代码。。。。。。。。。。具体步骤如下：

(首先mainFragment 中放置四个Fragment)
做movieFragment ，
当做到MovieFragment 的初始化视图initView 的时候，去做了两个监听类，一个作为基类，一个作为子类，如下：
BaseOnPageChangeListener为父类，DandyPagerChangeListener作为子类。
做到了movieFragment 的而在初始化数据initData的时候，要造一个集合fragments，放”热映Fragment，待映Fragment，海外Fragment” 这三个Fragment，作为数据准备。

在造第一个 热映Fragment 类时，别忘了在 加载布局 setContentView 的时候，里面有一个自定义控件UpdateDataListener 用以下拉刷新。
在下拉刷新自定义控件类的里面也是各种自定义控件，
如82行，自定义了一个HeadViewWeight ，在他的里面还有一个RefreshView
83行又定义了一个 RefreshView
88行又自定义了一个 FootViewWeight

刷新完了以后，接着回来做hotfragment  的initView 中的HotAdapter ，HotAdapter 是继承自commonAdapter 的，所以先建一个commonAdapter 。在建commonAdapter 的时候，还涉及到 ViewHolder 类的建立。

中间省略大段大段文字粘贴，至此才在MovieFragment类里的数组集合中完成添加一个 HotFragment。。。。
所以，待映Fragment 和 海外Fragment 都只是加了一个文字显示，没有加其他的东西。




做cinemaFragment ，
到了MainActivity 中的第二大Fragment 了，影院Fragment。
在一开始，继承BaseFragment ，实现新的一个自定义类：ViewClickListener。
内部私有一个对象属性：自定义的SelectedButton。
后来一想，现在在新版的猫眼电影没有这些东西，我整这没用的干什么，就没在整。直接加载了一个布局，然后什么也没做。

做discoverFragment ，
这个更简单了，因为新版这里没有什么可以自定义的了，直接就是一个TextView ：发现。

做mineFragment ，
在这里面最复杂：原APP展现，如图

最上面的红色框框里面的东西是自定义的RoundedImageView ，
第二栏”想看，看过，影评，话题”是自定义的 ImageDoubleTextView ，
第三栏类似于黑色框框的东西是用 View 展现出来的，
我的订单类似于下面的：
①”我的消息，会员中心，我的成就”；
②”优惠券，我的钱包，商城”；
③”设置”,；

“未消费，待付款，待评价，退款”也是用自定义控件 ImageDoubleTextView  实现。

在当点击以后可以实现水波纹的效果是添加了一个第三方库：
nineoldandroids-2.4.0.jar  来实现。

至此，大体框架搭建完毕。
，






 




 
