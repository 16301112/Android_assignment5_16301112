package com.example.zhaojp.test1;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.dyhdyh.compat.mmrc.MediaMetadataRetrieverCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link video.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link video#newInstance} factory method to
 * create an instance of this fragment.
 */
public class video extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private View view;
    private VideoView video;
    private Button buttonDownload;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public video() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment video.
     */
    // TODO: Rename and change types and number of parameters
    public static video newInstance(String param1, String param2) {
        video fragment = new video();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_video, container, false);
        //视频播放
        video = view.findViewById(R.id.videoView2);

        buttonDownload=view.findViewById(R.id.download);
        buttonDownload.setOnClickListener(new Button.OnClickListener() {
                                              @Override
                                              public void onClick(View v) {
                                                  Toast.makeText(getActivity(),"Long press the video list item to download", Toast.LENGTH_SHORT).show();
                                              }
                                          });

        /*//自动 - 推荐
                MediaMetadataRetrieverCompat mmrc = MediaMetadataRetrieverCompat.create();
                mmrc.setDataSource( "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4",new HashMap<String, String>() );
                String title = mmrc.extractMetadata(MediaMetadataRetrieverCompat.METADATA_KEY_TITLE);
                String album = mmrc.extractMetadata(MediaMetadataRetrieverCompat.METADATA_KEY_ALBUM);
                String albumArtist = mmrc.extractMetadata(MediaMetadataRetrieverCompat.METADATA_KEY_ALBUMARTIST);
                String author = mmrc.extractMetadata(MediaMetadataRetrieverCompat.METADATA_KEY_AUTHOR);
                String duration = mmrc.extractMetadata(MediaMetadataRetrieverCompat.METADATA_KEY_DURATION);
                //Bitmap bitmap=mmrc.getFrameAtTime();
                //Bitmap bitmap=mmrc.getFrameAtTime(0, MediaMetadataRetriever.OPTION_NEXT_SYNC);
                //String time=mmrc.extractMetadata( MediaMetadataRetriever.METADATA_KEY_DURATION);

                TextView t=view.findViewById(R.id.content);
                t.setText( "title:"+title+"  author"+author );
                /*ImageSpan imgSpan = new ImageSpan(getActivity(), bitmap);
                SpannableString spanString = new SpannableString("icon");
                spanString.setSpan(imgSpan, 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                t.setText(spanString);*/


        videoOnlineList();
        videoDownloadList();
        return view;
    }


    //播放视频
    private void initView() {
        //针对于本地视频
        //String path = Environment.getExternalStorageDirectory().getPath()+"/"+"https://www.bilibili.com/video/av20997550?spm_id_from=333.334.b_63686965665f7265636f6d6d656e64.1";//获取视频路径

        //针对于某个视频
        String path=/*"http://f.us.sinaimg.cn/003A3DQClx07nbwXOlkc01040201kjI30k010.mp4?label=mp4_hd&template=720x404.28&Expires=1545776862&ssig=qRtmaQ7aJv&KID=unistore,video"*/"http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";
        Uri uri = Uri.parse(path);//将路径转换成uri
        video.setVideoURI(uri);//为视频播放器设置视频路径
        video.setMediaController(new MediaController(getActivity()));//显示控制栏
        video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                video.start();//开始播放视频
            }
        });

        //针对于某个台
        /*String url = "http://ivi.bupt.edu.cn/hls/cctv15.m3u8";
        video.setVideoPath( url );
        video.requestFocus();
        video.start();
        video.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);*/
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    public void down(String url){
        //下载视频
        //final String path="http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";
        final String path=url;
        // 创建文件夹，在存储卡下
        String dirName = Environment.getExternalStorageDirectory().getPath() + "/Download/myVideo" ;
        File file = new File(dirName);
        // 文件夹不存在时创建
        if (!file.exists()) {
            file.mkdir();
        }

        // 下载后的文件名
        int i = path.lastIndexOf("/"); // 取的最后一个斜杠后的字符串为名
        final String fileName = dirName + path.substring(i, path.length());
        File file1 = new File(fileName);
        if (file1.exists()) {
            // 如果已经存在, 就不下载了, 去播放
            Toast.makeText(getActivity(),"It has been downloaded,reselect one,please", Toast.LENGTH_SHORT).show();
        } else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    downloadIt(path,fileName);
                }
            }).start();
        }
    }


    // 下载具体操作
    private void downloadIt(String path,String filename) {
        try {
            URL url = new URL(path);
            // 打开连接
            URLConnection conn = url.openConnection();
            // 打开输入流
            InputStream is = conn.getInputStream();
            // 创建字节流
            byte[] bs = new byte[1024];
            int len;
            OutputStream os = new FileOutputStream(filename);
            // 写数据
            while ((len = is.read(bs)) != -1) {
                os.write(bs, 0, len);
            }
            // 完成后关闭流
            Log.e(TAG, "download-finish");
            os.close();
            is.close();
            //            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "e.getMessage() --- " + e.getMessage());
        }
    }



    void videoOnlineList()
    {
        RecyclerView mRecyclerView;

        mRecyclerView =view.findViewById(R.id.videoOnline);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());


        List<String> name = new ArrayList();
        name.add("Video:Lionel Messi");
        name.add("Video;Miroslav Klose");
        name.add("Video:Roque Santa Cruz");
        name.add("Video:Eidur Gudjohnsen");
        name.add("Video:Alberto Gilardino");
        name.add("Video:David Beckham");

        List<String> item = new ArrayList();
        item.add("Time:2min");
        item.add("Time:3min");
        item.add("Time:2min");
        item.add("Time:2min");
        item.add("Time:1min");
        item.add("Time:1min");

        List<String> item2 = new ArrayList();
        item.add("Source:YOUKU");
        item.add("Source:YOUKU");
        item.add("Source:YOUKU");
        item.add("Source:YOUKU");
        item.add("Source:YOUKU");
        item.add("Source:YOUKU");


        //添加适配器，并监听每一个项目
        VideoOnlineAdapter mAdapter;

        mAdapter = new VideoOnlineAdapter(name,item,item, R.layout.activity_video_online,this.getActivity());

        mAdapter.setOnItemClickListener(new VideoOnlineAdapter.OnItemClickListener(){
            @Override
            public void onItemClick(View view, int position) {
                for(int i=0;i<6;i++){
                    if(position==i)
                    {
                        initView();
                    }
                }
            }
        });

        mAdapter.setOnItemLongClickListener(new VideoOnlineAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int position) {
                for(int i=0;i<6;i++){
                    if(position==i)
                    {
                        down("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4");
                    }
                }
            }
        });

        mRecyclerView.setAdapter(mAdapter);

        //分割线
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this.getActivity(),DividerItemDecoration.VERTICAL));
    }


    void videoDownloadList()
    {
        RecyclerView mRecyclerView;

        mRecyclerView =view.findViewById(R.id.videoDownload);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());


        List<String> name = new ArrayList();
        name.add("Video:Lionel Messi");

        List<String> item = new ArrayList();
        item.add("Time:2min");

        List<String> item2 = new ArrayList();
        item.add("Source:YOUKU");

        //添加适配器，并监听每一个项目
        VideoOnlineAdapter mAdapter;

        mAdapter = new VideoOnlineAdapter(name,item,item, R.layout.activity_video_online,this.getActivity());

        mAdapter.setOnItemClickListener(new VideoOnlineAdapter.OnItemClickListener(){
            @Override
            public void onItemClick(View view, int position) {
                if(position==0)
                {
                    String path = Environment.getExternalStorageDirectory().getPath()+"/Download/myVideo/"+"big_buck_bunny.mp4";//获取视频路径

                    //针对于某个视频
                    Uri uri = Uri.parse(path);//将路径转换成uri
                    video.setVideoURI(uri);//为视频播放器设置视频路径
                    video.setMediaController(new MediaController(getActivity()));//显示控制栏
                    video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            video.start();//开始播放视频
                        }
                    });
                }

            }
        });

        mRecyclerView.setAdapter(mAdapter);

        //分割线
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this.getActivity(),DividerItemDecoration.VERTICAL));
    }


}
