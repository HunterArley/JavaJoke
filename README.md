# JavaJoke
内涵段子项目学习

## 模板方法构造BaseActivity

```
/**
 * 整个应用的BaseActivity
 * 针对于MVC模式，MVP有区别
 * 放一些通用的代码
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置布局layout
        setContentView();
        //一些特定的算法，子类基本都会使用的
        ButterKnife.bind(this);
        //初始化头部
        initTitle();
        //初始化界面
        initView();
        //初始化数据
        initData();
    }

    //初始化数据
    protected abstract void initData();

    //初始化界面
    protected abstract void initView();

    //初始化头部
    protected abstract void initTitle();

    //设置布局layout
    protected abstract void setContentView();

    /**
     * 启动Activity
     */
    protected void startActivity(Class<?> clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }
```


```
public abstract class BaseSkinActivity extends BaseActivity {
    //后面会写其他功能，预留一层

}
```

